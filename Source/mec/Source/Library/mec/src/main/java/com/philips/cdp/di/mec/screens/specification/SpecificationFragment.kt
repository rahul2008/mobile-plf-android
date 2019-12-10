package com.philips.cdp.di.mec.screens.specification

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.databinding.MecProductSpecsFragmentBinding
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.prxclient.datamodels.specification.*


class SpecificationFragment : MecBaseFragment() {

    private lateinit var binding: MecProductSpecsFragmentBinding
    lateinit var prxSpecificationViewModel: SpecificationViewModel

    private val specificationObserver : Observer<SpecificationModel> = object : Observer<SpecificationModel> {

        override fun onChanged(specification: SpecificationModel?) {
            binding.specificationModel = specification
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductSpecsFragmentBinding.inflate(inflater, container, false)

        prxSpecificationViewModel = ViewModelProviders.of(this).get(SpecificationViewModel::class.java)

        prxSpecificationViewModel.mecError.observe(this,this)
        prxSpecificationViewModel.specification.observe(this,specificationObserver)

        val bundle = arguments
        val productCtn = bundle!!.getString(MECConstant.MEC_PRODUCT_CTN,"INVALID")

        context?.let { prxSpecificationViewModel.fetchSpecification(it,productCtn) }

        //TODO Default set
        val specificationModel = createDefaultValue()

        binding.specificationModel = createDefaultValue()

        return binding.root
    }

    private fun createDefaultValue(): SpecificationModel {
        val specificationModel = SpecificationModel()
        val data = Data()

        val csValueItem = CsValueItem()
        val csValueItems = mutableListOf<CsValueItem>()
        csValueItems.add(csValueItem)

        val csItemItem = CsItemItem()
        csItemItem.csValue = csValueItems
        val csItemItemList = mutableListOf<CsItemItem>()
        csItemItemList.add(csItemItem)

        val csChapterItems = mutableListOf<CsChapterItem>()

        val csChapterItem = CsChapterItem()
        csChapterItem.csItem = csItemItemList
        csChapterItems.add(csChapterItem)

        val mutableListOf = mutableListOf(csChapterItem)
        mutableListOf.add(csChapterItem)


        data.csChapter = csChapterItems
        specificationModel.data = data
        return specificationModel
    }
}