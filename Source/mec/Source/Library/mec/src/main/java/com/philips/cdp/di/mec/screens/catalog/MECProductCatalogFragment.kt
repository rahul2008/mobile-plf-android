package com. philips.cdp.di.mec.screens.catalog


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.products.ECSProducts

import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecCatalogFragmentBinding

import android.support.v7.widget.DefaultItemAnimator
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.uid.view.widget.Label

import kotlinx.android.synthetic.main.mec_main_activity.*


/**
 * A simple [Fragment] subclass.
 */
open class MECProductCatalogFragment : MecBaseFragment(),Pagination, ItemClickListener {



    private val productReviewObserver : Observer<MutableList<MECProductReview>> = Observer<MutableList<MECProductReview>> { mecProductReviews ->
        productReviewList.clear()
        mecProductReviews?.let { productReviewList.addAll(it) }
        adapter.notifyDataSetChanged()
        binding.progressBar.visibility = View.GONE
        hideProgressBar()
    }



    override fun onItemClick(item: Any) {

        val ecsProduct = item as MECProductReview
        val bundle = Bundle()
        bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT,ecsProduct.ecsProduct)

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        val fragment = MECProductDetailsFragment()
        fragment.arguments = bundle
        addFragment(fragment,"detail",true)
    }

    override fun isPaginationSupported(): Boolean {
        return shouldSupportPagination
    }

    private lateinit var mecCatalogUIModel: MECCatalogUIModel
    private  var highLightedBackgroundColor:Int = 0

    var totalPages: Int = 0
    var currentPage: Int = 0
    var pageSize: Int = 20
    var shouldSupportPagination = true


    private val productObserver : Observer<MutableList<ECSProducts>> = Observer<MutableList<ECSProducts>> { ecsProductsList ->
        if (! ecsProductsList .isNullOrEmpty()) {

            totalPages = ecsProductsList.get(ecsProductsList.size-1).pagination?.totalPages ?: 0

            currentPage = ecsProductsList.get(ecsProductsList.size-1).pagination?.currentPage ?: 0

            productList.clear()


            binding.productCatalogRecyclerView.visibility = View.VISIBLE

            for (ecsProducts in ecsProductsList) {

                for (ecsProduct in ecsProducts.products) {
                    productList.add(ecsProduct)
                }
            }

            ecsProductViewModel.fetchProductReview(productList)

            if (productList.size!=0 && MECDataHolder.INSTANCE.getPrivacyUrl() != null && (MECDataHolder.INSTANCE.locale.equals("de_DE") || MECDataHolder.INSTANCE.locale.equals("de_AT") ||MECDataHolder.INSTANCE.locale.equals("de_CH") || MECDataHolder.INSTANCE.locale.equals("sv_SE"))) {
                binding.mecPrivacyLayout.visibility = View.VISIBLE
                binding.mecSeparator.visibility = View.VISIBLE
                binding.mecLlLayout.visibility = View.VISIBLE
            }
        } else{
            binding.mecProductCatalogEmptyTextLabel.visibility = View.VISIBLE
            binding.productCatalogRecyclerView.visibility = View.GONE
            binding.mecPrivacyLayout.visibility = View.GONE
            binding.mecSeparator.visibility = View.GONE
            binding.mecLlLayout.visibility = View.GONE
            hideProgressBar()
        }

        if(productList.size!=0){
            binding.mecProductCatalogEmptyTextLabel.visibility = View.GONE
            binding.mecLlLayout.visibility = View.VISIBLE
            binding.llBannerPlaceHolder.visibility = View.VISIBLE
        } else{
            binding.mecProductCatalogEmptyTextLabel.visibility = View.VISIBLE
            binding.mecLlLayout.visibility = View.GONE
            binding.llBannerPlaceHolder.visibility = View.GONE
            hideProgressBar()
        }
    }

    private lateinit var adapter: MECProductCatalogBaseAbstractAdapter


    lateinit var ecsProductViewModel: EcsProductViewModel


    private lateinit var productReviewList: MutableList<MECProductReview>
    lateinit var productList: MutableList<ECSProduct>


    private lateinit var binding: MecCatalogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecCatalogFragmentBinding.inflate(inflater, container, false)

        mecCatalogUIModel = MECCatalogUIModel()


        ecsProductViewModel = ViewModelProviders.of(this).get(EcsProductViewModel::class.java)

        ecsProductViewModel.ecsProductsList.observe(this, productObserver)
        ecsProductViewModel.ecsProductsReviewList.observe(this, productReviewObserver)
        ecsProductViewModel.mecError.observe(this,this)

        val bundle = arguments

        highLightedBackgroundColor=getListAndGridHighlightedBackgroundColor()

        binding.mecGrid.setOnClickListener {
            if(null==binding.mecGrid.background || getBackgroundColorOfFontIcon(binding.mecGrid)==0) {//if Grid is currently not selected
                binding.mecGrid.setBackgroundColor(highLightedBackgroundColor)
                binding.mecList.setBackgroundColor(ContextCompat.getColor(binding.mecList.context, R.color.uidTransparent))
                adapter = MECProductCatalogGridAdapter(productReviewList, this)
                binding.productCatalogRecyclerView.layoutManager = GridLayoutManager(activity, 2)
                binding.productCatalogRecyclerView.adapter = adapter
                binding.productCatalogRecyclerView.setItemAnimator(DefaultItemAnimator())
                val Hdivider = DividerItemDecoration(binding.productCatalogRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL)
                val Vdivider = DividerItemDecoration(binding.productCatalogRecyclerView.getContext(), DividerItemDecoration.VERTICAL)
                binding.productCatalogRecyclerView.addItemDecoration(Hdivider)
                binding.productCatalogRecyclerView.addItemDecoration(Vdivider)
                adapter.notifyDataSetChanged()
            }
        }

        binding.mecList.setOnClickListener {
            if(null==binding.mecList.background || getBackgroundColorOfFontIcon(binding.mecList)==0) { //if Grid is currently not selected
                binding.mecList.setBackgroundColor(highLightedBackgroundColor)
                binding.mecGrid.setBackgroundColor(ContextCompat.getColor(binding.mecGrid.context, R.color.uidTransparent))
                adapter = MECProductCatalogListAdapter(productReviewList, this)
                binding.productCatalogRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                binding.productCatalogRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        productList = mutableListOf()
        productReviewList= mutableListOf()

        val mClearIconView = binding.mecSearchBox.getClearIconView()
        val searchText = binding.mecSearchBox.searchTextView
        binding.mecSearchBox.searchTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)

                if(s?.length == 0){
                    shouldSupportPagination = true
                    binding.llBannerPlaceHolder.visibility = View.VISIBLE
                }else {
                    shouldSupportPagination = false
                    binding.llBannerPlaceHolder.visibility = View.GONE
                }

                val text = String.format(context?.getResources()?.getText(R.string.mec_zero_results_found).toString(), s)
                binding.tvEmptyListFound.text = text

                if (MECDataHolder.INSTANCE.getPrivacyUrl() != null && (MECDataHolder.INSTANCE.locale.equals("de_DE") || MECDataHolder.INSTANCE.locale.equals("de_AT") ||MECDataHolder.INSTANCE.locale.equals("de_CH") || MECDataHolder.INSTANCE.locale.equals("sv_SE"))) {
                    if (adapter.itemCount != 0) {
                       binding.mecPrivacyLayout.visibility = View.VISIBLE
                   } else {
                       binding.mecPrivacyLayout.visibility = View.GONE
                   }
               }

                if (adapter.itemCount != 0) {
                    binding.mecEmptyResult.visibility = View.GONE
                } else {
                    binding.mecEmptyResult.visibility = View.VISIBLE
                }

                mClearIconView.setOnClickListener {
                    searchText.text.clear()
                        binding.mecEmptyResult.visibility = View.GONE
                }
            }

        })


        binding.mecNestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {


                if(shouldFetchNextPage())
                    binding.progressBar.visibility = View.VISIBLE
                    executeRequest()
            }
        })


        privacyTextView(binding.mecPrivacy)

        adapter = MECProductCatalogListAdapter(productReviewList,this)

        binding.productCatalogRecyclerView.adapter = adapter

        binding.productCatalogRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }


        return binding.root
    }

   private  fun getBackgroundColorOfFontIcon (label: Label):Int{
        val cd: ColorDrawable = label.background as ColorDrawable;
        val colorCode: Int  = cd.color;
        return colorCode

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createCustomProgressBar(container,MEDIUM)
        executeRequest()
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_product_catalog, true)
    }

    private fun privacyTextView(view: TextView) {
        val spanTxt = SpannableStringBuilder(
                getString(R.string.mec_read_privacy))
        spanTxt.append(" ")
        spanTxt.append(getString(R.string.mec_privacy_notice))
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPrivacyFragment()
                binding.progressBar.visibility = View.GONE
               // hideProgressBar()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
                ds.color = R.attr.uidHyperlinkDefaultPressedTextColor
            }
        }, spanTxt.length - getString(R.string.mec_privacy_notice).length, spanTxt.length, 0)
        spanTxt.append(" ")
        spanTxt.append(getString(R.string.mec_more_info))
        binding.mecPrivacy.setHighlightColor(Color.TRANSPARENT)
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }

    private fun showPrivacyFragment() {
        val bundle = Bundle()
        bundle.putString(MECConstant.MEC_PRIVACY_URL, MECDataHolder.INSTANCE.getPrivacyUrl())
        val mecPrivacyFragment = MecPrivacyFragment()
        mecPrivacyFragment.arguments = bundle
        addFragment(mecPrivacyFragment,"privacy",true)
    }



    public fun createInstance(args: Bundle): MECProductCatalogFragment {
        val fragment = MECProductCatalogFragment()
        fragment.arguments = args
        return fragment
    }


    private fun isScrollDown(lay: LinearLayoutManager): Boolean {
        val visibleItemCount = lay.childCount
        val firstVisibleItemPosition = lay.findFirstVisibleItemPosition()
        return visibleItemCount + firstVisibleItemPosition >= lay.itemCount && firstVisibleItemPosition >= 0
    }

    open fun executeRequest(){
        ecsProductViewModel.init(currentPage, pageSize)
    }

    fun shouldFetchNextPage(): Boolean{

        if(!isPaginationSupported()){
            return false
        }
        val lay = binding.productCatalogRecyclerView
                .layoutManager as LinearLayoutManager

        if (isScrollDown(lay)) {
            if (currentPage < totalPages) {
                currentPage = currentPage+1
              return true
            }
        }

        return false
    }

    override fun processError(mecError: MecError?){
        binding.mecProductCatalogEmptyTextLabel.visibility = View.VISIBLE
        binding.productCatalogRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun getListAndGridHighlightedBackgroundColor():Int{
        val numbers: IntArray = intArrayOf(R.attr.uidToggleButtonInputQuietNormalOnBackgroundColor)
        var typedArray:TypedArray  = context!!.obtainStyledAttributes(numbers)
        val colorCodeHighlighted:Int = typedArray.getColor(0,0)
        typedArray.recycle();
        return colorCodeHighlighted
    }

}


