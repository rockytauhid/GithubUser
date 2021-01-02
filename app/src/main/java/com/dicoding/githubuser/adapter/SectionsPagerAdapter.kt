package com.dicoding.githubuser.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.githubuser.R
import com.dicoding.githubuser.activity.FollowFragment
import com.dicoding.githubuser.activity.ReposFragment
import com.dicoding.githubuser.model.Companion


class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var followers: Int = 0
    var following: Int = 0
    var publicRepos: Int = 0

    @StringRes
    private val TABTITLES = intArrayOf(
        R.string.text_followers,
        R.string.text_following,
        R.string.text_repositories
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = Companion.newInstance(FollowFragment(), position)
            1 -> fragment = Companion.newInstance(FollowFragment(), position)
            2 -> fragment = ReposFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TABTITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}