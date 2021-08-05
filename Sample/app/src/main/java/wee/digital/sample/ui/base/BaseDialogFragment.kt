package wee.digital.sample.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import wee.digital.library.extension.hideKeyboard
import wee.digital.library.extension.hideSystemUI
import wee.digital.sample.R

abstract class BaseDialogFragment<B : ViewBinding> : DialogFragment(),
        FragmentView {

    protected val bind: B by viewBinding(inflating())

    abstract fun inflating(): (LayoutInflater) -> B

    /**
     * [DialogFragment] implements
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), dialogStyle()) {
            override fun onBackPressed() {
                this@BaseDialogFragment.onBackPressed()
            }
        }
        dialog.window?.also {
            it.decorView.setBackgroundColor(Color.TRANSPARENT)
            it.attributes.windowAnimations = R.style.App_DialogAnim
            onWindowConfig(it)
        }
        dialog.setOnDismissListener {
            println("onDismiss")
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = bind.root
        view.setOnTouchListener { _, _ ->
            dismiss()
            true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    override fun onStart() {
        super.onStart()
        when (dialogStyle()) {
            R.style.App_Dialog_FullScreen,
            R.style.App_Dialog_FullScreen_Transparent,
            -> dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        post(200) {
            super.onDismiss(dialog)
        }
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    /**
     * [BaseDialogFragment] properties
     */
    protected open fun dialogStyle(): Int {
        return R.style.App_Dialog_FullScreen
    }

    protected open fun onBackPressed() {
        dismissAllowingStateLoss()
    }

    protected open fun onWindowConfig(window: Window) = Unit

}