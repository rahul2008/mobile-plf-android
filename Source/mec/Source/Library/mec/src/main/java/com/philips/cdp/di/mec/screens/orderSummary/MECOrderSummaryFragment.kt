package com.philips.cdp.di.mec.screens.orderSummary


import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecOrderSummaryFragmentBinding
import com.philips.cdp.di.mec.payment.MECPayment
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MecPrivacyFragment
import com.philips.cdp.di.mec.screens.shoppingCart.MECCartSummary
import com.philips.cdp.di.mec.screens.shoppingCart.MECCartSummaryAdapter
import com.philips.cdp.di.mec.screens.shoppingCart.MECShoppingCartFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder


/**
 * A simple [Fragment] subclass.
 */
class MECOrderSummaryFragment : MecBaseFragment(), ItemClickListener {
    companion object {
        val TAG = "MECOrderSummaryFragment"
    }

    private lateinit var binding: MecOrderSummaryFragmentBinding
    private lateinit var ecsShoppingCart: ECSShoppingCart
    private lateinit var ecsAddress: ECSAddress
    private lateinit var mecPayment: MECPayment
    private var cartSummaryAdapter: MECCartSummaryAdapter? = null
    private var productsAdapter: MECOrderSummaryProductsAdapter? = null
    private var vouchersAdapter: MECOrderSummaryVouchersAdapter? = null
    private lateinit var cartSummaryList: MutableList<MECCartSummary>
    private lateinit var voucherList: MutableList<AppliedVoucherEntity>
    override fun onItemClick(item: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFragmentTag(): String {
        return "MECOrderSummaryFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MecOrderSummaryFragmentBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.shoppingCart
        ecsAddress = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESS) as ECSAddress
        ecsShoppingCart = arguments?.getSerializable(MECConstant.KEY_ECS_SHOPPING_CART) as ECSShoppingCart
        mecPayment = arguments?.getSerializable(MECConstant.MEC_PAYMENT_METHOD) as MECPayment
        binding.ecsAddressShipping = ecsAddress
        binding.shoppingCart = ecsShoppingCart
        binding.mecPayment = mecPayment
        cartSummaryList = mutableListOf()
        voucherList = mutableListOf()
        if (ecsShoppingCart.appliedVouchers.size > 0) {
            ecsShoppingCart.appliedVouchers?.let { voucherList.addAll(it) }
        }
        cartSummaryAdapter = MECCartSummaryAdapter(addCartSummaryList(ecsShoppingCart))
        productsAdapter = MECOrderSummaryProductsAdapter(ecsShoppingCart)
        vouchersAdapter = MECOrderSummaryVouchersAdapter(voucherList)
        binding.mecCartSummaryRecyclerView.adapter = productsAdapter
        binding.mecAcceptedCodeRecyclerView.adapter = vouchersAdapter
        binding.mecPriceSummaryRecyclerView.adapter = cartSummaryAdapter

        privacyTextView(binding.mecPrivacy)
        if (MECDataHolder.INSTANCE.getPrivacyUrl() != null) {
            binding.mecPrivacy.visibility = View.GONE
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_checkout, true)
    }

    private fun addCartSummaryList(ecsShoppingCart: ECSShoppingCart): MutableList<MECCartSummary> {
        cartSummaryList.clear()
        var name: String
        var price: String

        if (ecsShoppingCart.deliveryCost != null) {
            name = getString(R.string.mec_shipping_cost)
            price = ecsShoppingCart.deliveryCost.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))
        }

        for (i in 0 until ecsShoppingCart.appliedVouchers.size) {
            if (ecsShoppingCart.appliedVouchers.get(i).name == null) {
                name = " "
            } else {
                name = ecsShoppingCart.appliedVouchers.get(i).name
            }
            price = "-" + ecsShoppingCart.appliedVouchers?.get(i)?.appliedValue?.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))

        }

        cartSummaryAdapter?.notifyDataSetChanged()
        return cartSummaryList
    }

    fun onClickPay() {
        val bundle = Bundle()
        bundle.putSerializable(MECConstant.MEC_PAYMENT_METHOD, mecPayment.ecsPayment)
        val mecCvvBottomSheetFragment = MECCVVFragment()
        mecCvvBottomSheetFragment.arguments = bundle
        mecCvvBottomSheetFragment.setTargetFragment(this, MECConstant.PAYMENT_REQUEST_CODE)
        fragmentManager?.let { mecCvvBottomSheetFragment.show(it, mecCvvBottomSheetFragment.tag) }

    }

    fun onClickBackToShoppingCart() {
        fragmentManager!!.popBackStack(MECShoppingCartFragment.TAG, 0)
    }

    private fun privacyTextView(view: TextView) {
        val spanTxt = SpannableStringBuilder(
                getString(R.string.mec_read_privacy))
        spanTxt.append(" ")
        spanTxt.append(getString(R.string.mec_privacy))
        spanTxt.append(" ")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPrivacyFragment()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
                ds.color = R.attr.uidHyperlinkDefaultPressedTextColor
            }
        }, spanTxt.length - getString(R.string.mec_privacy).length-1, spanTxt.length, 0)
        spanTxt.append(getString(R.string.mec_questions))
        spanTxt.append(" ")
        spanTxt.append(getString(R.string.mec_faq))
        spanTxt.append(" ")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPrivacyFragment()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
                ds.color = R.attr.uidHyperlinkDefaultPressedTextColor
            }
        }, spanTxt.length - getString(R.string.mec_faq).length-1, spanTxt.length, 0)
        spanTxt.append(getString(R.string.mec_page))
        spanTxt.append(getString(R.string.mec_accept_terms))
        spanTxt.append(" ")
        spanTxt.append(getString(R.string.mec_terms_conditions))
        spanTxt.append(" ")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPrivacyFragment()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
                ds.color = R.attr.uidHyperlinkDefaultPressedTextColor
            }
        }, spanTxt.length - getString(R.string.mec_terms_conditions).length-1, spanTxt.length, 0)
        binding.mecPrivacy.highlightColor = Color.TRANSPARENT
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }

    private fun showPrivacyFragment() {
        val bundle = Bundle()
        bundle.putString(MECConstant.MEC_PRIVACY_URL, MECDataHolder.INSTANCE.getPrivacyUrl())
        val mecPrivacyFragment = MecPrivacyFragment()
        mecPrivacyFragment.arguments = bundle
        replaceFragment(mecPrivacyFragment, MecPrivacyFragment.TAG, true)
    }
}
