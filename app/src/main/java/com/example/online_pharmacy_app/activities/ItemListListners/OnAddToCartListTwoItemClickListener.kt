package com.example.online_pharmacy_app.activities.ItemListListners

import com.example.online_pharmacy_app.R
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnAddToCartListTwoItemClickListener : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
//        get() = ProductsByCategoryViewHolder::class

    override val viewId: ViewId
        get() = R.id.addToCartButton
}