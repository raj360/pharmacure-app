package com.example.online_pharmacy_app.activities.base.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.viewholders.DrugsViewHolder
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.databinding.FragmentHomeBinding
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Drug
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import smartadapter.SmartRecyclerAdapter

class HomeFragment : Fragment(R.layout.fragment_home),KodeinAware {

    override val kodein: Kodein by closestKodein()
    private lateinit var binding:FragmentHomeBinding
    private val factory:ViewModelFactory by instance()
    private lateinit var drugViewModel: DrugViewModel
    private lateinit var skeleton: Skeleton



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        drugViewModel= ViewModelProvider(this,factory).get(DrugViewModel::class.java)

        drugViewModel.getDrugList().observe(viewLifecycleOwner, Observer(::handleDrugResults))


        initializeSkeleton()
    }

    companion object{
        fun newInstance(): HomeFragment = HomeFragment()
    }



    private fun initializeSkeleton() {
        binding.recyclerViewDrugsHome.layoutManager = GridLayoutManager(context, 2)
        skeleton = binding.skeletonHome
        skeleton = binding.recyclerViewDrugsHome.applySkeleton(R.layout.item_drug_gridview, 6)
        skeleton.showSkeleton()

    }

    private fun handleDrugResults(result: SResult<List<Drug>>){
        when(result){
            is SResult.Loading->{
                log { "Loading..." }
            }

            is SResult.Success -> {
                log { "Result ${result.data}" }
                skeleton.showOriginal()
                SmartRecyclerAdapter
                    .items(result.data)
                    .map(Drug::class,DrugsViewHolder::class)
                    .setLayoutManager(GridLayoutManager(requireContext(),2))
                    .into<SmartRecyclerAdapter>(binding.recyclerViewDrugsHome)

            }
            is SResult.Error -> {
                log { "Error ${result.message} Message ${result.code}" }
            }

            is SResult.Empty -> {
                log { "Empty result" }
            }
        }
    }



}