package wee.digital.library.usb

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import wee.digital.library.app

object Usb {

    const val ERROR: String = "error"

    const val DETACHED: String = "is detached"

    const val ATTACHED: String = "is attached"

    const val GRANTED: String = "permission granted"

    const val DENIED: String = "permission denied"

    const val PERMISSION = ".USB_PERMISSION"

    val manager: UsbManager =
        ContextCompat.getSystemService(app, UsbManager::class.java) as UsbManager

    val intentFilter: IntentFilter = IntentFilter(PERMISSION).also {
        it.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        it.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        it.addAction(UsbManager.EXTRA_PERMISSION_GRANTED)
    }

    val deviceList: List<UsbDevice>
        get() {
            val list = mutableListOf<UsbDevice>()
            val map: HashMap<String, UsbDevice> = manager.deviceList
            map.forEach { list.add(it.value) }
            return list
        }

    fun getDevice(vendorId: Int): UsbDevice? {
        deviceList.forEach { if (vendorId == it.vendorId) return it }
        return null
    }

    fun observer(activity: AppCompatActivity, vararg vendorIds: Int) {
        val receiver = UsbReceiver(vendorIds)
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                activity.registerReceiver(receiver, intentFilter)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                activity.unregisterReceiver(receiver)
            }
        })
    }

    fun requestPermission(usb: UsbDevice) {
        if (!manager.hasPermission(usb)) {
            val intent = PendingIntent.getBroadcast(app, 1234, Intent(PERMISSION), 0)
            manager.requestPermission(usb, intent)
        }
    }

    fun requestPermission(vendorId: Int) {
        getDevice(vendorId)?.also {
            requestPermission(it)
        }
    }

    fun hasPermission(usb: UsbDevice): Boolean {
        return manager.hasPermission(usb)
    }

    fun forceClose(usb: UsbDevice?) {
        usb ?: return
        val connection = manager.openDevice(usb)
        for (i in usb.interfaceCount - 1 downTo 0) {
            connection.releaseInterface(usb.getInterface(i))
        }
        connection.close()
    }

    fun forceClose(vendorId: Int) {
        forceClose(getDevice(vendorId))
    }

    fun deviceStatus(vendorId: Int): String {
        return deviceStatus(getDevice(vendorId))
    }

    fun deviceStatus(usb: UsbDevice?): String {
        return when {
            null == usb -> DETACHED
            hasPermission(usb) -> GRANTED
            else -> ATTACHED
        }
    }

}