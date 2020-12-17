package com.maklumi.cats.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maklumi.cats.R
import com.maklumi.cats.view.MainActivity

class Notifikasi(val context: Context) {

    companion object {
        private const val CHANNEL_ID = "id saluran kucing"
        private const val NOTIS_ID = 3454309
    }

    fun rekaNotis() {
        rekaSaluranNotifikasi()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_cat_notifik)

        val notis = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cat_notifik)
            .setLargeIcon(icon)
            .setContentTitle("Kucing mari")
            .setContentText("Ada banyak jenis ")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIS_ID, notis)
    }

    private fun rekaSaluranNotifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val saluran = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Ini saluran notis"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(saluran)
        }
    }
}