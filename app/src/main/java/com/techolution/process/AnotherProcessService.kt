package com.techolution.process

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.techolution.aidldemo.IMyAidlInterface
import com.techolution.aidldemo.IMyAidlInterfaceCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class AnotherProcessService : Service() {

    companion object {
        const val TAG = "service1"
    }
    val myPid2 =  android.os.Process.myPid()
    private var responseCallBackMutable: IMyAidlInterfaceCallBack? = null
    private var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: called")
        context = this
    }

    private val mBinder = object : IMyAidlInterface.Stub() {
        override fun registerCallBack(callBack: IMyAidlInterfaceCallBack?): Boolean {
            responseCallBackMutable = callBack
            startIterativeProcess()
            return true
        }

        override fun unRegisterCallBack(): Boolean {
            responseCallBackMutable = null
            return true
        }

        override fun getPid(): Int {
            val myPid =  android.os.Process.myPid()
            Log.d(TAG, "getPid: $myPid")
            Log.d(TAG, "getPid2: $myPid2")
            return myPid
        }
    }

    private fun startIterativeProcess() {
        CoroutineScope(Dispatchers.IO).launch {
            while (responseCallBackMutable != null) {
                delay(500)
                responseCallBackMutable?.getPriceData((0..100).random(), (0..1100).random())
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }
}