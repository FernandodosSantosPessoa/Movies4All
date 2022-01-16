package com.example.movies4all.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.movies4all.R
import com.example.movies4all.databinding.DetailsFragmentBinding
import com.example.movies4all.viewmodel.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    val viewModel: DetailsViewModel by viewModel()
    private lateinit var binding: DetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.details_fragment, container, false
        )
        binding.lifecycleOwner = this

        val bundle = this.arguments

        val id = bundle?.getInt("id", 0)

        viewModel.getDetails(id)

        attachObserver()

        return binding.root
    }

    private fun attachObserver() {
        viewModel.movieDetails?.observe(viewLifecycleOwner, Observer {
            binding.detailTitle.text = it.original_title
            binding.detailOverview.text = it.overview
        })
    }

}