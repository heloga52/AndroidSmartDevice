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
import androidx.core.view.isVisible
import com.example.androidsmartdevice.databinding.ActivityDeviceBinding
import com.example.androidsmartdevice.databinding.ActivityMainBinding
import java.util.*

@SuppressLint("MissingPermission")

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothDevice: BluetoothDevice? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private val serviceUUID = UUID.fromString("0000feed-cc7a-482a-984a-7f2ed5b3e58f")
    private val characteristicLedUUID = UUID.fromString("0000abcd-8e22-4541-9d4c-21edae82ed19")
    private val characteristicButtonUUID = UUID.fromString("00001234-8e22-4541-9d4c-21edae82ed19")

    companion object {
        const val EXTRA_DEVICE = "DEVICE"
        const val TAG = "DeviceActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothDevice = intent.getParcelableExtra("device")

        bluetoothGatt = bluetoothDevice?.connectGatt(this, false, gattCallback)
        //bluetoothGatt?.connect()

        clickOnLed()

    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            //super.onConnectionStateChange(gatt, status, newState)
            Log.e("XXX", "Connected to GATT server.")
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e("OOOOO", "Connected to GATT server.")


                runOnUiThread {
                    displayContentConnected()
                    //binding.progressBar2.visibility = View.GONE
                    //Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
                }
                bluetoothGatt?.discoverServices()
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(bluetoothGatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(bluetoothGatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
// Enable notifications for the desired characteristic
                val characteristicButton3 = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicButtonUUID)
                bluetoothGatt?.setCharacteristicNotification(characteristicButton3, true)
                characteristicButton3?.descriptors?.forEach { descriptor ->
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    bluetoothGatt.writeDescriptor(descriptor)
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            if (characteristic.uuid == characteristicButtonUUID) {
                val value = characteristic.value
                val clicks = value[0].toInt()
                runOnUiThread {
                    binding.compteurClick.text = "Compteur de clique: ${clicks.toString()}"
                }
            }
        }
    }

    private fun displayContentConnected() {
        //binding.connexion.text = getString(R.string.connexion)
        //binding.progressBar2.visibility = View.VISIBLE
        binding.tpble.text = "TPBLE"
        binding.connexion.isVisible= false
        binding.affichage.isVisible = true
        binding.abonnement.isVisible = true
        binding.recevoir.isVisible = true
        binding.progressBar2.isVisible = false
        binding.led1.isVisible = true
        binding.led2.isVisible = true
        binding.led3.isVisible = true
        binding.compteurClick.isVisible = true
    }

    private fun clickOnLed(){
        binding.led1.setOnClickListener{

            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.led1.imageTintList == getColorStateList(R.color.teal_200)){
                binding.led1.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else{
                binding.led1.imageTintList = getColorStateList(R.color.teal_200)
                binding.led2.imageTintList = getColorStateList(R.color.black)
                binding.led3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x01)
                bluetoothGatt?.writeCharacteristic(characteristic)

            }
        }
        binding.led2.setOnClickListener{
            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.led2.imageTintList == getColorStateList(R.color.teal_200)){
                binding.led2.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else{
                binding.led2.imageTintList = getColorStateList(R.color.teal_200)
                binding.led1.imageTintList = getColorStateList(R.color.black)
                binding.led3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x02)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
        binding.led3.setOnClickListener{
            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.led3.imageTintList == getColorStateList(R.color.teal_200)){
                binding.led3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else{
                binding.led3.imageTintList = getColorStateList(R.color.teal_200)
                binding.led1.imageTintList = getColorStateList(R.color.black)
                binding.led2.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x03)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
    }
}

