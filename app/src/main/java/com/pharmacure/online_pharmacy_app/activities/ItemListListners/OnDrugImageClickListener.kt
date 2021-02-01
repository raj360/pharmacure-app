package com.pharmacure.online_pharmacy_app.activities.ItemListListners


import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.viewholders.DrugsViewHolder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.listener.OnItemClickListener

interface OnDrugImageClickListener : OnItemClickListener {
    override val viewHolderType: SmartViewHolderType
        get() = DrugsViewHolder::class

    override val viewId: ViewId
        get() = R.id.drug_item_gridView_constraint_layout
}