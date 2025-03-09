package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyPeriodicTimeWorkRequestWork(private var context: Context, @NonNull workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val TAG = "MyWorker"

    override fun doWork(): Result {
        displayNotification("My Worker", "Hey I finished my work")
        return Result.success()
    }

    private fun displayNotification(title:String, task:String) {
        Log.d(TAG, "doWork: Done")
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val id = getID()
        val notification = NotificationCompat.Builder(applicationContext, "simplifiedcoding")
            .setContentTitle(title)
            .setContentText("Update $task  id : $id")
            .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(id, notification.build())

    }
}