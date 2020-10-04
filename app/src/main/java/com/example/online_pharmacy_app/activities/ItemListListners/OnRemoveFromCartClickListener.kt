package com.example.online_pharmacy_app.activities.ItemListListners

import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.viewholders.CartViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnRemoveFromCartClickListener : OnItemClickListener {

    override val viewHolderType: SmartViewHolderType
        get() = CartViewHolder::class

    override val viewId: ViewId
        get() = R.id.removeFromCartButton


}