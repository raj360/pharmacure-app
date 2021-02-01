package com.pharmacure.online_pharmacy_app.activities.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.viewobjects.Order


import kotlinx.android.synthetic.main.item_order_details.view.*
import smartadapter.viewholder.SmartViewHolder

class PaymentDetailsViewHolder(parent: ViewGroup) : SmartViewHolder<Order>(
    LayoutInflater.from(parent.context).inflate(R.layout.item_order_details, parent, false)
) {

    override fun bind(item: Order) {
        itemView.orderReferenceTextView.text = item.txRef
        itemView.orderDateTextView.text = item.created_at
        itemView.orderStatusTextView.text = item.paymentStatus
        itemView.orderDeliveryMode.text  = item.deliveryMethod

    }

}
