package com.pharmacure.online_pharmacy_app.activities.ItemListListners

import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.viewholders.DrugByCategoryResultViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnAddToCartCategoryDrug : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
        get() = DrugByCategoryResultViewHolder::class

    override val viewId: ViewId
        get() = R.id.addToCartButton
}