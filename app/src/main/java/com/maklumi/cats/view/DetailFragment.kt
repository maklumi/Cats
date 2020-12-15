package com.maklumi.cats.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maklumi.cats.R
import com.maklumi.cats.databinding.FragmentDetailBinding
import com.maklumi.cats.viewmodel.DetailViewModel


class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetch()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cat.observe(viewLifecycleOwner) { cat ->
            cat?.let {
                binding.tvNameFragDetail.text = cat.name
                binding.tvTemperFragDetail.text = cat.temperament
                binding.tvDescriptionFragDetail.text = cat.description
                binding.tvLifeSpanFragDetail.text = cat.lifeSpan
                binding.tvOriginFragDetail.text = cat.origin
            }

        }
    }
}