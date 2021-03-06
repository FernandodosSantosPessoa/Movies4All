package com.example.movies4all.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies4all.R
import com.example.movies4all.databinding.FragmentHomeBinding
import com.example.movies4all.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), OnMovieClick {

    val viewModel:HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        listsOfMoviesObserver()

        return binding.root
    }

    private fun listsOfMoviesObserver() {
        viewModel.listOfMovies?.observe(viewLifecycleOwner, Observer { lists ->
            lists?.let {
                binding.rvListsOfMovies.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvListsOfMovies.adapter = ListsOfMoviesAdapter(requireContext(), this, lists)
            }
        })
    }

    override fun onClick(id: Int) {
        val detailsFrag = DetailsFragment()
        val args = Bundle()
        args.putInt("id", id)
        detailsFrag.arguments = args

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment,  detailsFrag, "findThisFragment")
            .addToBackStack(null)
            .commit()
    }
}