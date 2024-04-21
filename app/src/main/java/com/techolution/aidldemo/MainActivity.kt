package com.techolution.aidldemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "mainActivity"
    }

    private var mService:IMyAidlInterface?=null
    private val mConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "service connected: ${Thread.currentThread().name}")
            mService = IMyAidlInterface.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "service disconnected: ${Thread.currentThread().name}")
            mService = null
        }
    }
    private lateinit var startService: Button
    private lateinit var stopService:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService = findViewById(R.id.startService)
        stopService = findViewById(R.id.stopService)
        Thread.currentThread().name = "MainActivity"

        val intent = Intent(this,DemoService::class.java)
        bindService(intent,mConnection,Context.BIND_AUTO_CREATE)
        val myPid = android.os.Process.myPid()

        startService.setOnClickListener {
            Log.d(TAG, "main activity thread id :  ${Thread.currentThread().id}")
            Log.d(TAG, "main activity thread name: ${Thread.currentThread().name}")
            Log.d(DemoService.TAG, "processIdMAIN: $myPid")
            try {
                mService?.sendMessage("Hello from process B1")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        stopService.setOnClickListener {
            Log.d(TAG, "main activity thread id :  ${Thread.currentThread().id}")
        }


    }
}