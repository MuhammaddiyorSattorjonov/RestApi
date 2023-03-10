package com.example.restapigetandadd.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restapigetandadd.databinding.ItemTvBinding
import com.example.restapigetandadd.models.User

class RvAdapter(val rvAction: RvAction,val list: ArrayList<User> = ArrayList()) : RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemTvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: User, position: Int) {
            itemRvBinding.tvName.text = user.sarlavha
            itemRvBinding.tvDeadline.text = user.oxirgi_muddat
            itemRvBinding.tvStatus.text = user.holat
            itemRvBinding.delete.setOnClickListener {
                rvAction.deleteToDo(user)
            }

            itemRvBinding.edit.setOnClickListener {
                rvAction.updateToDo(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemTvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface RvAction{
        fun deleteToDo(user: User)
        fun updateToDo(user: User)
    }
}