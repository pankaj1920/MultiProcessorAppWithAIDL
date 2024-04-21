package com.techolution.aidldemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.techolution.aidldemo.databinding.ActivityMainBinding
import com.techolution.process.AnotherProcessService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "mainActivity"
    }

    private var serverProcess : IMyAidlInterface?=null
    private var isProcessActive = false



    private val mConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serverProcess = IMyAidlInterface.Stub.asInterface(service)
            isProcessActive = true
            enableDisableAllView(true)
            binding.startAnotherProcess.isEnabled = false
            Log.d(TAG, "onServiceConnected: ")
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            serverProcess = null
            isProcessActive = false
            enableDisableAllView(false)
            binding.startAnotherProcess.isEnabled = true
            Log.d(TAG, "onServiceConnected: ")
        }
    }


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.startAnotherProcess.setOnClickListener {
            initProcess()
        }

        binding.killAnotherProcess.setOnClickListener {
            if (serverProcess != null && isProcessActive){
                serverProcess?.pid?.let { it1 ->
                    Log.d(TAG, "onCreate: pid  $it1")
                    unbindService(mConnection)
                    android.os.Process.killProcess(it1)
                    serverProcess = null
                    isProcessActive = false
                    enableDisableAllView(false)
                    binding.startAnotherProcess.isEnabled = true
                }
            }
        }

        binding.registerCallBack.setOnClickListener {
            if (serverProcess!=null && isProcessActive){
                serverProcess?.registerCallBack(listenDataFromCallBack)
            }
        }

        binding.unRegisterCallBack.setOnClickListener {
            if (serverProcess!=null && isProcessActive){
                serverProcess?.unRegisterCallBack()
            }
        }

        binding.resetOperation.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (serverProcess!=null && isProcessActive){
                    serverProcess?.unRegisterCallBack()
                    delay(2000)
                    serverProcess?.registerCallBack(listenDataFromCallBack)
                }
            }
        }

    }


    private fun initProcess(){
        if (!isProcessActive){
            val intent = Intent(this, AnotherProcessService::class.java)
            val isEnabled = bindService(intent,mConnection,Context.BIND_AUTO_CREATE)
            enableDisableAllView(isEnabled)
            binding.startAnotherProcess.isEnabled = !isEnabled
        }
    }

    private fun enableDisableAllView(state:Boolean){
        binding.startAnotherProcess.isEnabled = state
        binding.registerCallBack.isEnabled = state
        binding.unRegisterCallBack.isEnabled = state
        binding.killAnotherProcess.isEnabled = state
        binding.resetOperation.isEnabled = state
    }


    private val listenDataFromCallBack = object : IMyAidlInterfaceCallBack.Stub(){
        override fun getPriceData(id: Int, price: Int) {
            Log.d(TAG, "getPriceData: $price $id")
        }

    }


}