package com.dicoding.githubuser.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.db.FavoriteDBContract
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User

internal class FavoriteWidgetRemoteViewFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var mCursor: Cursor? = null
    private var mWidgetItems = ArrayList<User>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        if (mCursor != null) {
            mCursor!!.close()
        }
        val identityToken = Binder.clearCallingIdentity()
        mCursor = mContext.contentResolver.query(
            FavoriteDBContract.FavoriteColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        ).also {
            mWidgetItems = MappingHelper.mapCursorToArrayList(it)
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        if (mCursor != null) {
            mCursor!!.close()
        }
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_favorite_widget)
        try {
            val bitmap: Bitmap = Glide.with(mContext)
                .asBitmap()
                .load(mWidgetItems[position].avatarUrl)
                .submit(300, 300)
                .get()
            rv.setImageViewBitmap(R.id.img_avatar_widget, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val extras = bundleOf(
            FavoriteWidget.EXTRA_ITEM to mWidgetItems[position].login
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.img_avatar_widget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}