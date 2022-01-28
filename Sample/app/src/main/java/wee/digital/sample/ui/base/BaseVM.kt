package wee.digital.sample.ui.base

import android.net.Network
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import wee.digital.library.extension.SimpleNetworkCallback
import wee.digital.library.extension.SingleLiveData
import wee.digital.library.extension.networkConnected
import wee.digital.library.extension.registerNetworkCallback
import wee.digital.library.util.Logger
import wee.digital.sample.data.api.ApiResultHandler
import wee.digital.widget.app


abstract class BaseVM : ViewModel() {

    val log by lazy { Logger(this::class.java.simpleName) }

    val networkAvailableLiveData: SingleLiveData<Boolean> by lazy {
        val liveData = SingleLiveData(networkConnected)
        registerNetworkCallback(object : SimpleNetworkCallback() {
            override fun onAvailable(network: Network) {
                viewModelScope.launch(Dispatchers.Main) {
                    delay(2000)
                    if (liveData.value != true) liveData.value = true
                }
            }
        })
        liveData
    }

    fun <T> Flow<Result<T?>>.onSuccess(block: T.() -> Unit): ApiResultHandler<T> {
        return ApiResultHandler(this)
            .onSuccess(block)
    }

    fun <T> Flow<Result<T?>>.launch(scope: CoroutineScope): ApiResultHandler<T> {
        return ApiResultHandler(this)
            .launch(scope)
    }

    fun <T> ApiResultHandler<T>.launch(): ApiResultHandler<T> {
        return launch(this@BaseVM.viewModelScope)
    }

    fun JsonObject.put(key: String, value: String?): JsonObject {
        if (null != value) addProperty(key, value)
        return this
    }

    fun string(@StringRes res: Int, vararg args: Any?): String {
        return try {
            String.format(app.getString(res), *args)
        } catch (ignore: Exception) {
            ""
        }
    }

}