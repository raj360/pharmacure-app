package com.example.online_pharmacy_app.activities.ItemListListners

import smartadapter.SmartViewHolderType
import smartadapter.listener.OnItemClickListener


interface OnListItemClickListener : OnItemClickListener {

    override val viewHolderType: SmartViewHolderType
        get() = super.viewHolderType
}