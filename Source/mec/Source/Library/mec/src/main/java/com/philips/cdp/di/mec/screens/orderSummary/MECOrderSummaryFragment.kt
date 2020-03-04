package com.philips.cdp.di.mec.screens.orderSummary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecOrderSummaryFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment


/**
 * A simple [Fragment] subclass.
 */
class MECOrderSummaryFragment : MecBaseFragment(), ItemClickListener {
    companion object {
        val TAG = "MECOrderSummaryFragment"
    }

    private lateinit var binding: MecOrderSummaryFragmentBinding
    lateinit var ecsOrderSummaryViewModel: EcsOrderSummaryViewModel
    override fun onItemClick(item: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFragmentTag(): String {
        return "MECOrderSummaryFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MecOrderSummaryFragmentBinding.inflate(inflater, container, false)
        binding.fragment = this
        ecsOrderSummaryViewModel = ViewModelProviders.of(this).get(EcsOrderSummaryViewModel::class.java)
        return binding.root
    }

    fun onOrderSummaryClick() {
//        showOrderSummaryFragment(TAG)
//        replaceFragment()
    }
}
