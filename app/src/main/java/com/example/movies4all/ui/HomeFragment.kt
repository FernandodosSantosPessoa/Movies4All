package com.example.movies4all.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.movies4all.R
import com.example.movies4all.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {


    val viewModel:HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel.listOfMovies?.observe(viewLifecycleOwner, Observer {
            makeToast()
        })

        return view
    }







    private fun makeToast() {
        Toast.makeText(context, "Fernando é gay!", Toast.LENGTH_LONG).show()
    }

}