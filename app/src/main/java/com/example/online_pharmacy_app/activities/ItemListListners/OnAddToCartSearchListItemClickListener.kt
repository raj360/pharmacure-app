package com.example.online_pharmacy_app.activities.ItemListListners

import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.viewholders.SearchResultDrugViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnAddToCartSearchListItemClickListener : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
        get() = SearchResultDrugViewHolder::class

    override val viewId: ViewId
        get() = R.id.addToCartButton
}