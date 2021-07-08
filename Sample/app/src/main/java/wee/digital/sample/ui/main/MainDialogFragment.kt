package wee.digital.sample.ui.main

import androidx.viewbinding.ViewBinding
import wee.digital.library.extension.activityVM
import wee.digital.sample.ui.base.BaseDialogFragment
import wee.digital.sample.ui.main.vm.DialogVM
import wee.digital.sample.ui.main.vm.MainVM

abstract class MainDialogFragment<B : ViewBinding> : BaseDialogFragment<B>(), MainView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by activityVM(MainVM::class)

    override val dialogVM by activityVM(DialogVM::class)

}