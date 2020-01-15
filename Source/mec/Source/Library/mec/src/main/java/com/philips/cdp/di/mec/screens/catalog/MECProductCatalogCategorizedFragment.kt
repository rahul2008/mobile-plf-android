package com.philips.cdp.di.mec.screens.catalog

import android.os.Bundle
import android.view.View
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uid.view.widget.AlertDialogFragment
import kotlinx.android.synthetic.main.mec_catalog_fragment.*
import kotlinx.android.synthetic.main.mec_main_activity.*

class MECProductCatalogCategorizedFragment : MECProductCatalogFragment() {

    lateinit var ctns : ArrayList<String>
    var totalProductsTobeSearched : Int = 0

    override fun executeRequest(){
        
        if(isAllProductsFound()) {
            binding.progressBar.visibility = View.GONE
            hideProgressBar()
        }else{
            ecsProductViewModel.initCategorized(currentPage, pageSize, ctns)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        ctns = arguments!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)
        totalProductsTobeSearched = ctns.size
        super.onActivityCreated(savedInstanceState)
    }

    override fun isPaginationSupported(): Boolean {
        return true
    }

    override fun showNoProduct() {

        currentPage += 1

        if (currentPage < totalPages) {
            showCategorizedFetchDialog()
        }else{
            super.showNoProduct()
        }
    }


    private fun showCategorizedFetchDialog(){

        val builder = AlertDialogFragment.Builder(context)
        builder.setCancelable(false)

        var alertDialogFragment = builder.create()
        builder.setMessage("Load More");
        builder.setPositiveButton("OK", fun(it: View) {

            if(productList.size==0) {
                createCustomProgressBar(container, MEDIUM)
            }else{
                binding.progressBar.visibility = View.VISIBLE
            }
            ecsProductViewModel.initCategorized(currentPage, pageSize, ctns)
            alertDialogFragment.dismiss()
        })


        builder.setNegativeButton("Cancel", fun(it: View) {
            super.showNoProduct()
            alertDialogFragment.dismiss()
        })

        builder.setTitle("Load More DATA")
        alertDialogFragment.show(fragmentManager,"ALERT_DIALOG_TAG")

    }

    override fun isCategorizedHybrisPagination(): Boolean {
        return true
    }

    override fun doProgressbarOperation() {

           if(productList.size ==0) return

           if(isCallEnded()){
               binding.progressBar.visibility = View.GONE
           }else{
               binding.progressBar.visibility = View.VISIBLE
           }
    }

    private fun didProductsFondReachPageSize() = productList.size % pageSize ==0

    private fun isProductNotFound() = productList.size == 0

    private fun isAllProductsFound() = totalProductsTobeSearched == productList.size

    private fun didReachThreshold() =  0 == (currentPage + 1) % MECConstant.THRESHOLD

    private fun didReachLastPage() = currentPage == totalPages - 1


    private fun isCallEnded(): Boolean {
        return  didReachLastPage( ) ||
                isAllProductsFound() ||
                didProductsFondReachPageSize()
    }


}

