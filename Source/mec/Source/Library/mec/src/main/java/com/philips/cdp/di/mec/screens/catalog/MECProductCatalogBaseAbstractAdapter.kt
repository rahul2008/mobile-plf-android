package com.philips.cdp.di.mec.screens.catalog

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.philips.cdp.di.ecs.model.products.ECSProduct


abstract class MECProductCatalogBaseAbstractAdapter(private var items: MutableList<ECSProduct>) : RecyclerView.Adapter<MECProductCatalogAbstractViewHolder>(),Filterable {

    val originalList = items

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MECProductCatalogAbstractViewHolder, position: Int) = holder.bind(items[position])


    override fun getFilter(): Filter {

        var filteredList :MutableList<ECSProduct> = mutableListOf()

        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()
                val filterResults = FilterResults()
                filterResults.values = originalList

                if(searchString.isEmpty()){
                    filteredList = originalList
                }else{

                    for(mecProducts in originalList){

                        if(mecProducts.code.contains(searchString,true) || mecProducts.name.contains(searchString,true)){
                            filteredList.add(mecProducts)
                        }
                    }
                }
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results?.values as MutableList<ECSProduct>
                notifyDataSetChanged()
            }

        }
    }
}