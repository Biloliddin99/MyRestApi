package com.example.myrestapi


//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.myrestapi.adapters.MyRvAdapter
import com.example.myrestapi.adapters.RvClick
import com.example.myrestapi.databinding.ActivityMainBinding
import com.example.myrestapi.databinding.ItemDialogBinding
import com.example.myrestapi.models.MyTodo
import com.example.myrestapi.models.MyTodoPostRequest
import com.example.myrestapi.models.TodoPostRequest
import com.example.myrestapi.repository.TodoRepository
import com.example.myrestapi.retrofit.ApiClient
import com.example.myrestapi.utils.Status
import com.example.myrestapi.viewmodel.MyViewModelFactory
import com.example.myrestapi.viewmodel.TodoViewModel
import java.util.Calendar


class MainActivity : AppCompatActivity(), RvClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var myRvAdapter: MyRvAdapter
    private lateinit var todoRepository: TodoRepository
    private val TAG = "MainActivity"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoRepository = TodoRepository(ApiClient.getApiService())
        todoViewModel =
            ViewModelProvider(this, MyViewModelFactory(todoRepository))[TodoViewModel::class.java]
        myRvAdapter = MyRvAdapter(rvClick = this)
        binding.myRv.adapter = myRvAdapter
        todoViewModel.getAllTodo()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "onCreate: Loading")
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.ERROR -> {
                        Log.d(TAG, "onCreate: Error ${it.message}")
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }

                    Status.SUCCESS -> {
                        Log.d(TAG, "onCreate: ${it.data}")
                        myRvAdapter.list = it.data!!
                        myRvAdapter.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
            dialog.setView(itemDialogBinding.root)

            itemDialogBinding.editSana.visibility = View.GONE
            val zarurlikItems = arrayOf("shart", "foydali", "hayot_mamot", "tavsiya")
            val myAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, zarurlikItems)
            myAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            itemDialogBinding.spinnerZarurlik.adapter = myAdapter

            itemDialogBinding.btnSelectDate.setOnClickListener {
                val currentDate = Calendar.getInstance()
                val year = currentDate.get(Calendar.YEAR)
                val month = currentDate.get(Calendar.MONTH)
                val day = currentDate.get(Calendar.DAY_OF_MONTH)
                val datePicker = DatePickerDialog(
                    this,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate =
                            "$selectedYear-${selectedMonth + 1}-$selectedDay"
                        itemDialogBinding.btnSelectDate.text = selectedDate
                    },
                    year,
                    month,
                    day
                )
                datePicker.show()
            }

            itemDialogBinding.btnSave.setOnClickListener {
                val todoPostRequest = TodoPostRequest(
                    itemDialogBinding.editSarlavha.text.toString(),
                    itemDialogBinding.editBatafsil.text.toString(),
                    itemDialogBinding.btnSelectDate.text.toString(),
                    itemDialogBinding.spinnerZarurlik.selectedItem.toString(),
                    itemDialogBinding.checkBajarildi.toString().toBoolean()
                )
                todoViewModel.addTodo(todoPostRequest).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            itemDialogBinding.progressBar.visibility = View.VISIBLE
                            itemDialogBinding.linerDialog.isEnabled = false
                        }

                        Status.ERROR -> {
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                            itemDialogBinding.progressBar.visibility = View.GONE
                            itemDialogBinding.linerDialog.isEnabled = true
                        }

                        Status.SUCCESS -> {
                            Toast.makeText(
                                this,
                                "${it.data?.sarlavha} ${it.data?.id}ga saqlandi",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.cancel()
                        }
                    }
                }
            }
            dialog.show()
        }
    }

    override fun menuClick(imageView: ImageView, myTodo: MyTodo) {
        val popup = PopupMenu(this, imageView)
        popup.inflate(com.example.myrestapi.R.menu.todo_menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                com.example.myrestapi.R.id.menu_edit -> {
                    editTodo(myTodo)
                }

                com.example.myrestapi.R.id.menu_delete -> {
                    deleteTodo(myTodo)
                }
            }
            true
        }
        popup.show()
    }

    private fun editTodo(myTodo: MyTodo) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
        dialog.setView(itemDialogBinding.root)
        itemDialogBinding.editSana.visibility = View.GONE
        val zarurlikItems = arrayOf("shart", "foydali", "hayot_mamot", "tavsiya")
        val myAdapter = ArrayAdapter(
            this,
            R.layout.simple_list_item_1,
            zarurlikItems
        )
        myAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        itemDialogBinding.spinnerZarurlik.adapter = myAdapter
        itemDialogBinding.editSarlavha.setText(myTodo.sarlavha)
//        itemDialogBinding.editSana.setText(myTodo.sana)
        itemDialogBinding.editBatafsil.setText(myTodo.batafsil)
        itemDialogBinding.checkBajarildi.isChecked = myTodo.bajarildi
        itemDialogBinding.btnSelectDate.text = myTodo.oxirgi_muddat
        val selectedItemPosition = zarurlikItems.indexOf(myTodo.zarurlik)
        itemDialogBinding.spinnerZarurlik.setSelection(selectedItemPosition)

        itemDialogBinding.btnSelectDate.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate =
                        "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    itemDialogBinding.btnSelectDate.text = selectedDate
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        itemDialogBinding.btnSave.setOnClickListener {
            val myTodoPostRequest = MyTodoPostRequest(
                itemDialogBinding.checkBajarildi.toString().toBoolean(),
                itemDialogBinding.editBatafsil.text.toString(),
                itemDialogBinding.btnSelectDate.toString(),
                itemDialogBinding.editSarlavha.text.toString(),
                itemDialogBinding.spinnerZarurlik.selectedItem.toString()
            )

            todoViewModel.updateMyTodo(myTodo.id, myTodoPostRequest)
                .observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            itemDialogBinding.progressBar.visibility = View.VISIBLE
                            itemDialogBinding.linerDialog.isEnabled = false
                        }

                        Status.ERROR -> {
                            itemDialogBinding.progressBar.visibility = View.GONE
                            itemDialogBinding.linerDialog.isEnabled = true
                            Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                        }

                        Status.SUCCESS -> {
                            Toast.makeText(
                                this,
                                "${it.data?.sarlavha} saqlandi",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.cancel()
                        }
                    }
                }
        }
        dialog.show()

    }

    private fun deleteTodo(myTodo: MyTodo) {
        todoViewModel.deleteTodo(myTodo.id)
    }
}