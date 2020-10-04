package com.example.online_pharmacy_app.activities.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.viewobjects.Cart
import kotlinx.android.synthetic.main.item_drug_cart.view.*
import smartadapter.viewholder.SmartViewHolder

class CartViewHolder(parent: ViewGroup) : SmartViewHolder<Cart>(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_drug_cart, parent, false
    )
) {

    override fun bind(item: Cart) {


        itemView.quantityTextView.text = item.quantity.toString()
        itemView.categoryDrugName.text = item.drugName
        itemView.drug_price.text = item.costPrice.toString()
        itemView.productCategory.text = item.caTname
        Glide.with(itemView.categoryDrugImageView).load(item.getImageUrlString())
            .into(itemView.categoryDrugImageView)
    }

    override fun unbind() {
        super.unbind()
        Glide.with(itemView.categoryDrugImageView).clear(itemView.categoryDrugImageView)
    }


}