package com.example.online_pharmacy_app.activities.ItemListListners


import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.viewholders.DrugsViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnAddToCartGridListItemClickListener : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
        get() = DrugsViewHolder::class

    override val viewId: ViewId
        get() = R.id.addToCartButton
}