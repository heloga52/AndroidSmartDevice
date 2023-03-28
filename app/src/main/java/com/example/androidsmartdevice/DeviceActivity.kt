package com.example.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.androidsmartdevice.databinding.ActivityDeviceBinding
import com.example.androidsmartdevice.databinding.ActivityMainBinding
import java.util.*

@SuppressLint("MissingPermission")

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothDevice: BluetoothDevice? = null
    private var bluetoothGatt: BluetoothGatt? = null

    companion object {
        const val EXTRA_DEVICE = "DEVICE"
        const val TAG = "DeviceActivity"
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothDevice = intent.getParcelableExtra("device_address")

        bluetoothGatt = bluetoothDevice?.connectGatt(this, false, gattCallback)
        bluetoothGatt?.connect()

    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Connected to GATT server.")
                displayContentConnected()

            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

        }
    }

    private fun displayContentConnected() {
        binding.connexion.text = getString(R.string.connexion)
        binding.progressBar2.visibility = View.VISIBLE
    }
}

