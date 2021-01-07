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


class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TABS = intArrayOf(
        R.string.text_followers,
        R.string.text_following,
        R.string.text_repositories
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            2 -> ReposFragment()
            else -> Companion.newInstance(FollowFragment(), position)
        }
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TABS[position])
    }

    override fun getCount(): Int {
        return TABS.size
    }
}