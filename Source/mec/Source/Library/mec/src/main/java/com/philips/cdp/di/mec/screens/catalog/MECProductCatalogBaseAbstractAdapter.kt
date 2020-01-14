package com.philips.cdp.di.mec.screens.catalog

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.bazaarvoice.bvandroidsdk.Statistics
import com.philips.cdp.di.ecs.model.products.ECSProduct
import java.text.DecimalFormat


abstract class MECProductCatalogBaseAbstractAdapter(private var items: MutableList<MECProductReview>) : RecyclerView.Adapter<MECProductCatalogAbstractViewHolder>(),Filterable {

    val originalList = items

    lateinit var emptyView:View
    var catalogView = CatalogView.LIST

    var isSearchInProgress = true

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder

    override fun getItemCount(): Int {
     return  items.size
    }

    override fun onBindViewHolder(holder: MECProductCatalogAbstractViewHolder, position: Int) {

        holder.bind(items[position])
    }


    override fun getFilter(): Filter {

        var filteredList: MutableList<MECProductReview> = mutableListOf()

        return object : Filter() {


            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()


                if (searchString.isEmpty()) {
                    filteredList = originalList
                } else {
                    for (mecProducts in originalList) {

                        if (mecProducts.ecsProduct.code.contains(searchString, true) || mecProducts.ecsProduct.summary.productTitle.contains(searchString, true)) {
                            filteredList.add(mecProducts)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results?.values as MutableList<MECProductReview>
                if(items.size == 0){
                    emptyView.visibility = View.VISIBLE
                }else{
                    emptyView.visibility = View.GONE
                }
                notifyDataSetChanged()
            }

        }
    }


    enum class CatalogView{

         GRID,
         LIST
    }

}