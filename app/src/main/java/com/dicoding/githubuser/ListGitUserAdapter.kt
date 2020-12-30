package com.dicoding.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.databinding.ItemRowGituserBinding

class ListGitUserAdapter(private val listGitUsers: ArrayList<GitUser>): RecyclerView.Adapter<ListGitUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowGituserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listGitUsers[position])
    }

    override fun getItemCount(): Int = listGitUsers.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(private val binding: ItemRowGituserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GitUser) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgAvatar)
                txtLogin.text = user.login

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: GitUser)
    }
}