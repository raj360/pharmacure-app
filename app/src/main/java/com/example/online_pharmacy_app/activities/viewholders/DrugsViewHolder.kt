package com.example.online_pharmacy_app.activities.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.viewobjects.Drug
import kotlinx.android.synthetic.main.item_drug_gridview.view.*
import smartadapter.viewholder.SmartViewHolder

class DrugsViewHolder(parent: ViewGroup) : SmartViewHolder<Drug>(
    LayoutInflater.from(parent.context).inflate(R.layout.item_drug_gridview, parent, false)
) {

    override fun bind(item: Drug) {
        itemView.drugName.text = item.drugName
        itemView.drugPrice.text = item.unitPrice.toString()
        itemView.drugDescription.text = item.description


        Glide.with(itemView.drug_image)
            .load(item.getDrugImageUrl())
            .into(itemView.drug_image)
    }

    override fun unbind() {
        super.unbind()
        Glide.with(itemView.drug_image).clear(itemView.drug_image)
    }
}