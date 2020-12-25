package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_gituser_details.*

class GitUserDetailsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gituser_details)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val gitUser = intent.getParcelableExtra<GitUser>(Companion.EXTRA_USER)
        Glide.with(this).load(gitUser?.avatar).into(img_item_avatar)
        tv_item_name.text = gitUser?.name
        tv_item_username.text = gitUser?.username
        tv_item_company.text = gitUser?.company
        tv_item_location.text = gitUser?.location
        tv_item_repository.text = gitUser?.repository.toString()
        tv_item_follower.text = gitUser?.follower.toString()
        tv_item_following.text = gitUser?.following.toString()

        btn_set_share.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_set_share -> {
                val gitUser = intent.getParcelableExtra<GitUser>(Companion.EXTRA_USER)
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, gitUser?.username)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }
    }
}