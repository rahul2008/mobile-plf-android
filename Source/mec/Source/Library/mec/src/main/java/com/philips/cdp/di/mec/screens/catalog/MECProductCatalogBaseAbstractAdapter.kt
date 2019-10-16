package com.philips.cdp.di.mec.screens.catalog

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable


abstract class MECProductCatalogBaseAbstractAdapter(private var items: MutableList<MECProduct>) : RecyclerView.Adapter<MECProductCatalogAbstractViewHolder>(),Filterable {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MECProductCatalogAbstractViewHolder, position: Int) = holder.bind(items[position])


    override fun getFilter(): Filter {

        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()
                val filterResults = FilterResults()
                filterResults.values = items
                var filteredList :MutableList<MECProduct> = mutableListOf()

                if(searchString.isEmpty()){

                }else{

                    for(mecProducts in items){

                        if(mecProducts.code.contains(searchString,false)){
                            filteredList.add(mecProducts)
                        }
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results?.values as MutableList<MECProduct>
               notifyDataSetChanged()
            }

        }
    }
}