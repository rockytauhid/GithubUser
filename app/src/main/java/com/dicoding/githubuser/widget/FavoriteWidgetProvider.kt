package com.dicoding.githubuser.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.dicoding.githubuser.R
import com.dicoding.githubuser.activity.DetailActivity
import com.dicoding.githubuser.activity.FavoriteActivity

class FavoriteWidgetProvider : AppWidgetProvider() {
    companion object {
        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, FavoriteWidgetRemoveViewService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.favorite_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            // click event handler for the title, launches the app when the user clicks on title
            val titleIntent = Intent(context, FavoriteActivity::class.java)
            val titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0)
            views.setOnClickPendingIntent(R.id.banner_text, titlePendingIntent)

            // click event handler for the images, launches the app when the user clicks on image
            val detailIntent = Intent(context, DetailActivity::class.java)
            val detailPendingIntent = PendingIntent.getActivity(context, 0, detailIntent, 0)
            views.setPendingIntentTemplate(R.id.stack_view, detailPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}