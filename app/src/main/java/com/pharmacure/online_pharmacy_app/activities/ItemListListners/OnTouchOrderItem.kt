package com.pharmacure.online_pharmacy_app.activities.ItemListListners

import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.viewholders.PaymentDetailsViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnTouchOrderItem : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
        get() = PaymentDetailsViewHolder::class

    override val viewId: ViewId
        get() = R.id.orderCardLayout
}