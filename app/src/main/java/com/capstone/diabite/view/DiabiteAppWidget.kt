package com.capstone.diabite.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.widget.RemoteViews
import com.capstone.diabite.R
import com.capstone.diabite.db.local.HistoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiabiteAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            AppWidgetManager.getInstance(context)
                .notifyAppWidgetViewDataChanged(appWidgetIds, R.layout.diabite_app_widget)
            CoroutineScope(Dispatchers.IO).launch {
                val dao = HistoryDatabase.getDatabase(context).historyDao()
                val latestHistory = dao.getLatestPrediction()

                withContext(Dispatchers.Main) {
                    appWidgetIds.forEach { appWidgetId ->
                        val views = RemoteViews(context.packageName, R.layout.diabite_app_widget)

                        if (latestHistory != null) {
                            views.setTextViewText(R.id.tv_result, latestHistory.summary)
                            views.setTextViewText(R.id.progressText, "${latestHistory.prediction}%")
                            views.setProgressBar(
                                R.id.circularProgressView,
                                100,
                                latestHistory.prediction,
                                false
                            )
                        } else {
                            views.setTextViewText(R.id.tv_result, "No data")
                            views.setTextViewText(R.id.progressText, "--%")
                            views.setProgressBar(R.id.circularProgressView, 100, 0, false)
                        }

                        val bitmap = drawCircularProgress(latestHistory?.prediction ?: 0)
                        views.setImageViewBitmap(R.id.circularProgressImage, bitmap)

                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        val pendingIntent = PendingIntent.getActivity(
                            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }

                    val intent = Intent(context, DiabiteAppWidget::class.java).apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    }
                    val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                        ComponentName(context, DiabiteAppWidget::class.java)
                    )
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                    context.sendBroadcast(intent)
                }
            }
        }
    }

    private fun drawCircularProgress(progress: Int): Bitmap {
        val size = 110
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintBackground = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
            shader = SweepGradient(
                (size / 2).toFloat(), (size / 2).toFloat(),
                intArrayOf(Color.parseColor("#4D00E5FF"), Color.parseColor("#4DD500F9"), Color.parseColor("#4D00E5FF")),
                null
            )
        }

        val paintProgress = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
            shader = SweepGradient(
                (size / 2).toFloat(), (size / 2).toFloat(),
                intArrayOf(Color.parseColor("#00E5E5"), Color.parseColor("#E961FF"), Color.parseColor("#00E5E5")),
                null
            )
        }

        val rect = RectF(10f, 10f, (size - 10).toFloat(), (size - 10).toFloat())
        canvas.drawArc(rect, 0f, 360f, false, paintBackground)

        canvas.rotate(-90f, (size / 2).toFloat(), (size / 2).toFloat())

        val sweepAngle = (progress / 100f) * 360f
        canvas.drawArc(rect, 0f, sweepAngle, false, paintProgress)

        return bitmap
    }
}