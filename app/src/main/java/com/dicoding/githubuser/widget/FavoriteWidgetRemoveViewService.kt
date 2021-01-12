package com.dicoding.githubuser.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteWidgetRemoveViewService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        FavoriteWidgetRemoteViewFactory(this.applicationContext)
}