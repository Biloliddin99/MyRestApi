package com.example.myrestapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrestapi.databinding.ItemRvBinding
import com.example.myrestapi.models.MyTodo

class MyRvAdapter(var list: List<MyTodo> = emptyList(),val rvClick: RvClick) :
    RecyclerView.Adapter<MyRvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: ItemRvBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBind(myTodo: MyTodo, position: Int) {
            itemRvBinding.batafsil.text = myTodo.batafsil
            itemRvBinding.sana.text = myTodo.sana
            itemRvBinding.bajarildi.text=  myTodo.bajarildi.toString()
            itemRvBinding.oxirgiMuddat.text = myTodo.oxirgi_muddat
            itemRvBinding.zarurlik.text = myTodo.zarurlik
            itemRvBinding.sarlavha.text = myTodo.sarlavha

            itemRvBinding.more.setOnClickListener {
                rvClick.menuClick(itemRvBinding.more,myTodo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

}
interface RvClick{
    fun menuClick(imageView: ImageView,myTodo: MyTodo)
}