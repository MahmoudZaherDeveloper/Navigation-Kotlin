package com.zaher.navigationapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.zaher.navigationapplication.R
import com.zaher.navigationapplication.databinding.ListItemBinding
import com.zaher.navigationapplication.model.pojo.Posts
import com.zaher.navigationapplication.view.interfaces.ItemClickListener
import kotlinx.android.synthetic.main.list_item.view.*

class PostsListAdapter(val postsList: ArrayList<Posts>) :
    RecyclerView.Adapter<PostsListAdapter.DogViewHolder>(), ItemClickListener {

    fun updateDogsList(newDogsList: List<Posts>) {
        postsList.clear()
        postsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ListItemBinding>(
            layoutInflater,
            R.layout.list_item,
            parent,
            false
        )
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.model = postsList[position]
        holder.view.listener = this
    }

    override fun getItemCount() = postsList.size

    class DogViewHolder(var view: ListItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onItemClickListener(v: View) {
        val uuid = v.uuidTextView.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment()
        action.uuid = uuid
        Navigation.findNavController(v).navigate(action)
    }
}