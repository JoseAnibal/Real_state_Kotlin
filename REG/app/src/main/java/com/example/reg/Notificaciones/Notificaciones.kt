package com.example.reg.Notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reg.R
import java.util.concurrent.atomic.AtomicInteger


class Notificaciones(val contexto: Context) {

    val APP_ID = "com.example.reg"
    val CHANNEL_ID = "${APP_ID}_c1"
    var id = AtomicInteger(0)
    fun create_notification_id(): Int {
        return id.incrementAndGet()
    }

    lateinit var builder: NotificationCompat.Builder

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = contexto.getString(R.string.canalNotis)
            val descriptionText = contexto.getString(R.string.canalNotisDesc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                contexto.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun crearNotificacionIncidencia(titulo:String,desc:String) {
        builder = NotificationCompat.Builder(contexto, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_warning_24)
            .setContentTitle(titulo)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(contexto)) {
            notify(create_notification_id(), builder.build())
        }
    }


}