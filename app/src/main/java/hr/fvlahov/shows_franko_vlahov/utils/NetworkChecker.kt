package hr.fvlahov.shows_franko_vlahov.utils

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.util.concurrent.Executors

class NetworkChecker {
    companion object {
        const val GOOGLE_DNS = "8.8.8.8"
        const val DNS_PORT = 53
        const val TIMEOUT = 1500
        const val TAG = "NetworkChecker"
    }

    fun checkInternetConnectivity(): Boolean {
        return try {
            val sock = Socket()
            val socketAddress: SocketAddress = InetSocketAddress(GOOGLE_DNS, DNS_PORT)
            sock.connect(socketAddress, TIMEOUT)
            sock.close()
            Log.d(TAG, "Network connection available")
            true
        } catch (e: IOException) {
            Log.d(TAG, "Network connection not available")
            false
        }
    }
}