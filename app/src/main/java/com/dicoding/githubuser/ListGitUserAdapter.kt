package com.dicoding.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.databinding.ItemRowGituserBinding

class ListGitUserAdapter(): RecyclerView.Adapter<ListGitUserAdapter.ListViewHolder>() {

    private val mData = ArrayList<GitUser>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<GitUser>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_gituser, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        viewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowGituserBinding.bind(itemView)
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
        fun onItemClicked(user: GitUser)
    }
}