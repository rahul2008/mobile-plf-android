package com.philips.cdp.di.mec.screens.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecImagePagerItemBinding

class ImageAdapter(val assets: List<Asset>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return assets.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        var binding = MecImagePagerItemBinding.inflate(inflater)
        val asset = assets.get(position)

        val getHeightAndWidth = GetHeightAndWidth(binding.root.context).invoke()

        asset.asset + "?wid=" + getHeightAndWidth.width +
                "&hei=" + getHeightAndWidth.height + "&\$pnglarge$" + "&fit=fit,1"

        binding.asset = asset
        binding.executePendingBindings()
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return assets.get(position % assets.size).asset
    }

    private inner class GetHeightAndWidth(val context: Context) {

        var width: Int = 0
            private set
        var height: Int = 0
            private set

        internal operator fun invoke(): GetHeightAndWidth {
            width = 0
            height = 0
            width = context?.getResources()?.displayMetrics?.widthPixels ?: 0
            height = context?.getResources()?.getDimension(R.dimen.iap_product_detail_image_height)?.toInt()
                    ?:0

            return this
        }
    }
}