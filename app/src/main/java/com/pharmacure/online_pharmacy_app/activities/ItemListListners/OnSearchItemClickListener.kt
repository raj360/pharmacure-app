package com.pharmacure.online_pharmacy_app.activities.ItemListListners


import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.viewholders.SearchResultDrugViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnSearchItemClickListener : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
        get() = SearchResultDrugViewHolder::class

    override val viewId: ViewId
        get() = R.id.searchItemCard
}