package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import mente.vali.dailyweather.R
import mente.vali.dailyweather.databinding.FragmentHeaderBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class HeaderFragment : Fragment() {
    private val forecastViewModel: ForecastViewModel by lazy {
        ViewModelProviders.of(this).get(ForecastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHeaderBinding.inflate(inflater, container, false)
//        DataBindingUtil.inflate<FragmentHeaderBinding>(
//            inflater, R.layout.fragment_header,
//            container, false
//        )

        binding.lifecycleOwner = this
//        binding.viewmodel = forecastViewModel
        binding.viewmodel = (activity as MainActivity).getVM()
        return binding.root

    }


}
