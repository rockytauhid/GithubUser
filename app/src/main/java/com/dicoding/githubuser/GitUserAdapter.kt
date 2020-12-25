package com.dicoding.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class GitUserAdapter internal constructor(private val context: Context) : BaseAdapter() {

    internal var gitUsers = arrayListOf<GitUser>()

    override fun getCount(): Int = gitUsers.size

    override fun getItem(i: Int): Any = gitUsers[i]

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_gituser, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val gitUser = getItem(position) as GitUser
        viewHolder.bind(gitUser)
        return itemView
    }

    private class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val txtUsername: TextView = view.findViewById(R.id.txt_username)
        private val txtCompany: TextView = view.findViewById(R.id.txt_company)
        private val imgAvatar: CircleImageView = view.findViewById(R.id.img_avatar)

        internal fun bind(gitUser: GitUser) {
            txtName.text = gitUser.name
            txtUsername.text = gitUser.username
            txtCompany.text = gitUser.company
            imgAvatar.setImageResource(gitUser.avatar)
        }
    }
}