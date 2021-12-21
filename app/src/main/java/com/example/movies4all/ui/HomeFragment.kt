package com.example.movies4all.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movies4all.R
import com.example.movies4all.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        (requireActivity() as MainActivity).mainComponent.inject(this)
//    }

    val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        return view
    }
}