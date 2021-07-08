package wee.digital.sample.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import wee.digital.sample.R
import wee.digital.widget.extension.ViewClickListener

interface BaseView {

    val baseActivity: BaseActivity<*>? get() = null

    val lifecycleOwner: LifecycleOwner

    fun navController(): NavController?

    fun onViewCreated()

    fun onLiveDataObserve()

    fun addClickListener(vararg views: View?) {
        val listener = object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
        views.forEach { it?.setOnClickListener(listener) }
    }

    fun onViewClick(v: View?) = Unit

    fun navigate(@IdRes actionId: Int, args: Bundle? = null, extras: Navigator.Extras? = null, block: (NavOptions.Builder.() -> Unit) = {}) {
        val options = NavOptions.Builder().also {
            it.setVerticalAnim()
            it.block()
        }.build()
        navController()?.navigate(actionId, args, options, extras)
    }

    fun navigate(directions: NavDirections, args: Bundle? = null, extras: Navigator.Extras? = null, block: (NavOptions.Builder.() -> Unit) = {}) {
        navigate(directions.actionId, args, extras, block)
    }

    fun navigateUp() {
        navController()?.navigateUp()
    }

    fun NavOptions.Builder.setParallaxAnim(reserved: Boolean = false) {
        if (reserved) {
            setEnterAnim(R.anim.parallax_pop_enter)
            setExitAnim(R.anim.parallax_pop_exit)
            setPopEnterAnim(R.anim.parallax_enter)
            setPopExitAnim(R.anim.parallax_exit)
        } else {
            setEnterAnim(R.anim.parallax_enter)
            setExitAnim(R.anim.parallax_exit)
            setPopEnterAnim(R.anim.parallax_pop_enter)
            setPopExitAnim(R.anim.parallax_pop_exit)
        }
    }

    fun NavOptions.Builder.setHorizontalAnim(reserved: Boolean = false) {
        if (reserved) {
            setEnterAnim(R.anim.horizontal_pop_enter)
            setExitAnim(R.anim.horizontal_pop_exit)
            setPopEnterAnim(R.anim.horizontal_enter)
            setPopExitAnim(R.anim.horizontal_exit)
        } else {
            setEnterAnim(R.anim.horizontal_enter)
            setExitAnim(R.anim.horizontal_exit)
            setPopEnterAnim(R.anim.horizontal_pop_enter)
            setPopExitAnim(R.anim.horizontal_pop_exit)
        }

    }

    fun NavOptions.Builder.setVerticalAnim(): NavOptions.Builder {
        setEnterAnim(R.anim.vertical_enter)
        setExitAnim(R.anim.vertical_exit)
        setPopEnterAnim(R.anim.vertical_pop_enter)
        setPopExitAnim(R.anim.vertical_pop_exit)
        return this
    }

    fun NavOptions.Builder.setLaunchSingleTop(): NavOptions.Builder {
        setLaunchSingleTop(true)
        navController()?.graph?.id?.also {
            setLaunchSingleTop(true)
            setPopUpTo(it, false)
        }
        return this
    }

    val defaultArgKey: String get() = "DEFAULT_ARG_KEY"

    fun <T> navResultLiveData(key: String = defaultArgKey): MutableLiveData<T>? {
        return navController()?.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
    }

    fun <T> setNavResult(key: String?, result: T) {
        navController()?.previousBackStackEntry?.savedStateHandle?.set(key
                ?: defaultArgKey, result)
    }

    fun <T> setNavResult(result: T) {
        navController()?.previousBackStackEntry?.savedStateHandle?.set(defaultArgKey, result)
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(lifecycleOwner, Observer(block))
    }

    fun <T> LiveData<T>.removeObservers() {
        removeObservers(lifecycleOwner)
    }

    fun add(fragment: Fragment, stack: Boolean = true) {
        baseActivity?.add(fragment, stack)
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        baseActivity?.replace(fragment, stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

}