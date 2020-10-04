package com.example.online_pharmacy_app.activities.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.viewobjects.Drug
import kotlinx.android.synthetic.main.item_drug_cart.view.categoryDrugImageView
import kotlinx.android.synthetic.main.item_drug_list_small_view.view.*
import smartadapter.viewholder.SmartViewHolder

class DrugByCategoryResultViewHolder(parent: ViewGroup) : SmartViewHolder<Drug>(
    LayoutInflater.from(parent.context).inflate(R.layout.item_drug_list_small_view, parent, false)
) {
    override fun bind(item: Drug) {
        itemView.catDrugDescription.text =item.description

        itemView.catDrugName.text = item.drugName
        itemView.cateDrugPrice.text = item.unitPrice.toString()
        itemView.catDrugCategory.text = item.subName
        Glide.with(itemView.categoryDrugImageView)
            .load(item.getDrugImageUrl())
            .into(itemView.categoryDrugImageView)
    }

    override fun unbind() {
        super.unbind()
        Glide.with(itemView.categoryDrugImageView).clear(itemView.categoryDrugImageView)
    }


}
