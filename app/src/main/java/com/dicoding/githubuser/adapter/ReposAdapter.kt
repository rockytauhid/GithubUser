package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ItemRowReposBinding
import com.dicoding.githubuser.model.Repo

class ReposAdapter: RecyclerView.Adapter<ReposAdapter.ListViewHolder>() {

    private val mData = ArrayList<Repo>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<Repo>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_repos, viewGroup, false)
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
        private val binding = ItemRowReposBinding.bind(itemView)
        fun bind(repo: Repo) {
            binding.txtRepos.text = repo.name

            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(repo.htmlUrl.toString()) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(htmlUrl: String)
    }
}