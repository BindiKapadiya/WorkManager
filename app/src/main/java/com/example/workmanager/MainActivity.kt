package com.example.workmanager

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var oneTimeRequest: WorkRequest
    private lateinit var mConstraints: Constraints
    private lateinit var mWorkManager: WorkManager
    private lateinit var mWorkId: UUID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mWorkManager = WorkManager.getInstance(this)

        findViewById<Button>(R.id.button).setOnClickListener(this)
        findViewById<Button>(R.id.withStorageLow).setOnClickListener(this)
        findViewById<Button>(R.id.withBatteryLow).setOnClickListener(this)
        findViewById<Button>(R.id.withRequiresCharging).setOnClickListener(this)
        findViewById<Button>(R.id.withDeviceIdle).setOnClickListener(this)
        findViewById<Button>(R.id.withRequiredNetwork).setOnClickListener(this)
        findViewById<Button>(R.id.withPeriodic).setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
                oneTimeRequest =
                    OneTimeWorkRequest.Builder(MyOneTimeWorkRequestWork::class.java).build()
                mWorkId = oneTimeRequest.id
            }

            R.id.withStorageLow -> {
                mConstraints = Constraints.Builder().setRequiresStorageNotLow(true).build()
                oneTimeRequest = OneTimeWorkRequest.Builder(MyOneTimeWorkRequestWork::class.java)
                    .setConstraints(mConstraints).build()
                mWorkId = oneTimeRequest.id
            }

            R.id.withBatteryLow -> {
                mConstraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
                oneTimeRequest = OneTimeWorkRequest.Builder(MyOneTimeWorkRequestWork::class.java)
                    .setConstraints(mConstraints).build()
                mWorkId = oneTimeRequest.id
            }

            R.id.withRequiresCharging -> {
                mConstraints = Constraints.Builder().setRequiresCharging(true).build()
                oneTimeRequest = OneTimeWorkRequest.Builder(MyOneTimeWorkRequestWork::class.java)
                    .setConstraints(mConstraints).build()
                mWorkId = oneTimeRequest.id
            }

            R.id.withDeviceIdle -> {
                mConstraints = Constraints.Builder().setRequiresDeviceIdle(true).build()
                oneTimeRequest = OneTimeWorkRequest.Builder(MyOneTimeWorkRequestWork::class.java)
                    .setConstraints(mConstraints).build()
                mWorkId = oneTimeRequest.id
            }

            R.id.withRequiredNetwork -> {
                mConstraints =
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                oneTimeRequest = OneTimeWorkRequest.Builder(MyOneTimeWorkRequestWork::class.java)
                    .setConstraints(mConstraints).build()
                mWorkId = oneTimeRequest.id
            }

            R.id.withPeriodic -> {
                oneTimeRequest = PeriodicWorkRequest.Builder(
                    MyPeriodicTimeWorkRequestWork::class.java,
                    15,
                    TimeUnit.MINUTES
                )
                    .addTag(MainActivity::class.java.simpleName)
                    .build()
                mWorkManager.enqueue(oneTimeRequest)
                mWorkId = oneTimeRequest.id
            }

        }

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(mWorkId)
            .observe(this, Observer<WorkInfo?> { workInfo ->
                workInfo?.let {
                    findViewById<TextView>(R.id.txtStatus).append(workInfo.state.name + "\n")
                }
            })
        mWorkManager.enqueue(oneTimeRequest)
    }
}
