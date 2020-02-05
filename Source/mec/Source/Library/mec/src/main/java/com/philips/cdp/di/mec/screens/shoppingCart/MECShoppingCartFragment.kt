package com.philips.cdp.di.mec.screens.shoppingCart


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecShoppingCartFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.address.AddAddressFragment
import com.philips.cdp.di.mec.utils.AlertListener
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.UIPicker
import kotlinx.android.synthetic.main.mec_main_activity.*
import android.widget.Toast
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.auth.HybrisAuth
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class MECShoppingCartFragment : MecBaseFragment(),AlertListener {

    var mRootView: View? = null
    companion object {
        val TAG = "MECShoppingCartFragment"
    }

    internal var swipeController: MECSwipeController? = null

    private lateinit var binding: MecShoppingCartFragmentBinding
    private var itemPosition: Int = 0
    private var mPopupWindow: UIPicker? = null
    private lateinit var shoppingCart: ECSShoppingCart
    lateinit var ecsShoppingCartViewModel: EcsShoppingCartViewModel
    private var productsAdapter: MECProductsAdapter? = null
    private lateinit var productReviewList: MutableList<MECCartProductReview>

    private val cartObserver: Observer<ECSShoppingCart> = Observer<ECSShoppingCart> { ecsShoppingCart ->
        binding.shoppingCart = ecsShoppingCart
        shoppingCart = ecsShoppingCart!!

        if (ecsShoppingCart!!.entries.size != 0) {
            binding.mecEmptyResult.visibility = View.GONE
            binding.mecParentLayout.visibility = View.VISIBLE
            ecsShoppingCartViewModel.fetchProductReview(ecsShoppingCart.entries)
        } else if(ecsShoppingCart.entries.size == 0) {
            binding.mecEmptyResult.visibility = View.VISIBLE
            binding.mecParentLayout.visibility = View.GONE
        }
        if (ecsShoppingCart != null) {
            val quantity = MECutility.getQuantity(ecsShoppingCart)
            updateCount(quantity)
        }
        hideProgressBar()


    }

    private val productReviewObserver: Observer<MutableList<MECCartProductReview>> = Observer { mecProductReviews ->
        productReviewList.clear()
        mecProductReviews?.let { productReviewList.addAll(it) }
        productsAdapter?.notifyDataSetChanged()
        if(productsAdapter!=null){
            //MECProductsAdapter.CloseWindow(this.mPopupWindow).onStop()
        }
        hideProgressBar()
     }


    private val addressObserver: Observer<List<ECSAddress>> = Observer(fun(addressList: List<ECSAddress>?) {
        hideProgressBar()
        //if(addressList.isNullOrEmpty()){
            replaceFragment(AddAddressFragment(),"AddAddressFragment",false)
        //}else{

        //}

    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(null==mRootView) {
            binding = MecShoppingCartFragmentBinding.inflate(inflater, container, false)
            binding.fragment = this
            ecsShoppingCartViewModel = activity!!.let { ViewModelProviders.of(it).get(EcsShoppingCartViewModel::class.java) }

            ecsShoppingCartViewModel.ecsShoppingCart.observe(this, cartObserver)
            ecsShoppingCartViewModel.ecsProductsReviewList.observe(this, productReviewObserver)
            ecsShoppingCartViewModel.ecsAddresses.observe(this, addressObserver)

            val bundle = arguments
            //ecsShoppingCart = bundle?.getSerializable(MECConstant.MEC_SHOPPING_CART) as ECSShoppingCart

            productReviewList = mutableListOf()

            productsAdapter = MECProductsAdapter(productReviewList, this)

            binding.mecCartSummaryRecyclerView.adapter = productsAdapter

//        binding.mecCartSummaryRecyclerView.apply {
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        }

            swipeController = MECSwipeController(binding.mecCartSummaryRecyclerView.context, object : SwipeControllerActions() {
                override fun onRightClicked(position: Int) {
                    itemPosition = position
                    showDialog()
                }

                override fun onLeftClicked(position: Int) {
                    itemPosition = position
                    showDialog()
                }
            })


            val itemTouchHelper = ItemTouchHelper(swipeController!!)
            itemTouchHelper.attachToRecyclerView(binding.mecCartSummaryRecyclerView)

            binding.mecCartSummaryRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeController!!.onDraw(c)
                }
            })
            mRootView=binding.root
            getECSConfig()
        }
        return binding.root
    }

    fun showDialog(){
        MECutility.showActionDialog(binding.enterVoucherLabel.context,getString(R.string.mec_remove_product),getString(R.string.mec_cancel),getString(R.string.mec_delete_item), getString(R.string.mec_delete_item_description),fragmentManager!!,this )
    }

    override fun onPositiveBtnClick() {
        updateCartRequest(shoppingCart.entries.get(itemPosition),0)
    }

    override fun onNegativeBtnClick() {

    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_shopping_cart, true)
        setCartIconVisibility(false)
    }

    override fun onStart() {
        super.onStart()
//        if (ecsShoppingCart.entries.size != 0) {
//            createCustomProgressBar(container, MEDIUM)
//            ecsShoppingCartViewModel.fetchProductReview(ecsShoppingCart.entries)
//        }

    }

     fun executeRequest() {
        createCustomProgressBar(container, MEDIUM)
        ecsShoppingCartViewModel.getShoppingCart()
    }

    fun updateCartRequest(entries: ECSEntries, int: Int){
        createCustomProgressBar(container,MEDIUM)
        ecsShoppingCartViewModel.updateQuantity(entries,int)
    }

   /* fun onClick() {
        ecsShoppingCartViewModel.fetchAddresses()
    }*/

    fun onCheckOutClick(){
        createCustomProgressBar(container, MEDIUM)
        ecsShoppingCartViewModel.fetchAddresses()
    }

    fun gotoProductCatalog(){
        showProductCatalogFragment(MECShoppingCartFragment.TAG)
    }

    private fun getECSConfig(){
        MecHolder.INSTANCE.eCSServices.configureECSToGetConfiguration(object: ECSCallback<ECSConfig, Exception> {

            override fun onResponse(config: ECSConfig?) {
                if (MECDataHolder.INSTANCE.hybrisEnabled) {
                    MECDataHolder.INSTANCE.hybrisEnabled = config?.isHybris ?: return

                }
                MECDataHolder.INSTANCE.locale = config!!.locale
                MECAnalytics.setCurrencyString(MECDataHolder.INSTANCE.locale)
                if(null!=config!!.rootCategory){
                    MECDataHolder.INSTANCE.rootCategory = config!!.rootCategory
                }
                executeRequest()
                GlobalScope.launch{
                    HybrisAuth.authHybrisIfNotAlready()
                }

            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        } )
    }
}
