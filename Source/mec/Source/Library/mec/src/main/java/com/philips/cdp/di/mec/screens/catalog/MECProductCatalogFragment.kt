package com. philips.cdp.di.mec.screens.catalog


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.products.ECSProducts

import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecCatalogFragmentBinding

import android.support.v7.widget.DefaultItemAnimator
import android.widget.RelativeLayout
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant

import kotlinx.android.synthetic.main.mec_main_activity.*


/**
 * A simple [Fragment] subclass.
 */
open class MECProductCatalogFragment : MecBaseFragment(),Pagination, ItemClickListener {


    override fun onItemClick(item: Object) {

        val ecsProduct = item as ECSProduct
        val bundle = Bundle()
        bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT,ecsProduct)

        val fragment = MECProductDetailsFragment()
        fragment.arguments = bundle
        replaceFragment(fragment,"detail",true)
    }

    override fun isPaginationSupported(): Boolean {
        return true
    }

    private lateinit var mecCatalogUIModel: MECCatalogUIModel
    val TAG = MECProductCatalogFragment::class.java.name

    var totalPages: Int = 0
    var currentPage: Int = 0
    var pageSize: Int = 8


    val productObserver : Observer<MutableList<ECSProducts>> = object : Observer<MutableList<ECSProducts>> {


        override fun onChanged(ecsProductsList: MutableList<ECSProducts>?) {
            hideProgressBar()

            totalPages = ecsProductsList?.get(ecsProductsList.size-1)?.pagination?.totalPages ?: 0

            currentPage = ecsProductsList?.get(ecsProductsList.size-1)?.pagination?.currentPage ?: 0

            productList.clear()

        if (ecsProductsList != null) {
            binding.mecProductCatalogEmptyTextLabel.visibility = View.GONE
            binding.productCatalogRecyclerView.visibility = View.VISIBLE

            for (ecsProducts in ecsProductsList) {

                for (ecsProduct in ecsProducts.products) {
                    productList.add(ecsProduct)
                }
            }
        } else{
            binding.mecProductCatalogEmptyTextLabel.visibility = View.VISIBLE
            binding.productCatalogRecyclerView.visibility = View.GONE
        }
        currentPage++
        adapter.notifyDataSetChanged()

        }
    }

    private lateinit var adapter: MECProductCatalogBaseAbstractAdapter


    lateinit var ecsProductViewModel: EcsProductViewModel


    lateinit var productList: MutableList<ECSProduct>

    private lateinit var binding: MecCatalogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecCatalogFragmentBinding.inflate(inflater, container, false)

        mecCatalogUIModel = MECCatalogUIModel()


        ecsProductViewModel = ViewModelProviders.of(this).get(EcsProductViewModel::class.java)

        ecsProductViewModel.ecsProductsList.observe(this, productObserver)
        ecsProductViewModel.mecError.observe(this,this)

        val bundle = arguments



        binding.mecGrid.setOnClickListener {
            binding.mecGrid.setBackgroundColor(Color.parseColor("#DCDCDC"))
            binding.mecList.setBackgroundColor(Color.parseColor("#ffffff"))
            adapter = MECProductCatalogGridAdapter(productList,this)
            binding.productCatalogRecyclerView.layoutManager = GridLayoutManager(activity, 2)
            binding.productCatalogRecyclerView.adapter = adapter
            binding.productCatalogRecyclerView.setItemAnimator(DefaultItemAnimator())
            val Hdivider = DividerItemDecoration(binding.productCatalogRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL)
            val Vdivider = DividerItemDecoration(binding.productCatalogRecyclerView.getContext(), DividerItemDecoration.VERTICAL)
            binding.productCatalogRecyclerView.addItemDecoration(Hdivider)
            binding.productCatalogRecyclerView.addItemDecoration(Vdivider)
            adapter.notifyDataSetChanged()
        }

        binding.mecList.setOnClickListener {
            binding.mecList.setBackgroundColor(Color.parseColor("#DCDCDC"))
            binding.mecGrid.setBackgroundColor(Color.parseColor("#ffffff"))
            adapter = MECProductCatalogListAdapter(productList,this)
            binding.productCatalogRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding.productCatalogRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        productList = mutableListOf<ECSProduct>()

        binding.mecSearchBox.setSearchBoxHint("Search")
        binding.mecSearchBox.setDecoySearchViewHint("Search")

        binding.mecSearchBox.searchTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)

                if(s?.length == 0){
                    binding.llBannerPlaceHolder.visibility = View.VISIBLE
                }else binding.llBannerPlaceHolder.visibility = View.GONE
            }

        })


        binding.productCatalogRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)


                if(shouldFetchNextPage())
                    executeRequest()

            }
        })

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        executeRequest()
    }
    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_product_catalog, true)
    }

    override fun handleBackEvent(): Boolean {
        return super.handleBackEvent()
    }


    public fun createInstance(args: Bundle): MECProductCatalogFragment {
        val fragment = MECProductCatalogFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MECProductCatalogListAdapter(productList,this)

        binding.productCatalogRecyclerView.adapter = adapter

        binding.productCatalogRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }


    private fun isScrollDown(lay: LinearLayoutManager): Boolean {
        val visibleItemCount = lay.childCount
        val firstVisibleItemPosition = lay.findFirstVisibleItemPosition()
        return visibleItemCount + firstVisibleItemPosition >= lay.itemCount && firstVisibleItemPosition >= 0
    }

    open fun executeRequest(){
        createCustomProgressBar(container, MEDIUM, RelativeLayout.ALIGN_PARENT_BOTTOM)
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
              return true
            }
        }

        return false;
    }

    open fun enableLoadMore() : Boolean{
        return false
    }

    override fun processError(mecError: MecError?){
        super.processError(mecError)
        binding.mecProductCatalogEmptyTextLabel.visibility = View.VISIBLE
        binding.productCatalogRecyclerView.visibility = View.GONE
    }

}


