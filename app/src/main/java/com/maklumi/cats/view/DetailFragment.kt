package com.maklumi.cats.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.maklumi.cats.databinding.FragmentDetailBinding
import com.maklumi.cats.util.lukisanPutaran
import com.maklumi.cats.util.muatturun
import com.maklumi.cats.viewmodel.DetailViewModel
import java.lang.NumberFormatException
import kotlin.math.round


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

        viewModel.fetch(args.catUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cat.observe(viewLifecycleOwner) { cat ->
            cat?.let {
                binding.cat = cat
//                binding.ivCatFragDetail.muatturun(
//                    cat.image,
//                    lukisanPutaran(binding.ivCatFragDetail.context)
//                )
                binding.tvWeightFragDetail.text = poundsToKgConversion(cat.weight)
//                binding.tvNameFragDetail.text = cat.name
//                binding.tvTemperFragDetail.text = cat.temperament
//                binding.tvDescriptionFragDetail.text = cat.description
//                binding.tvLifeSpanFragDetail.text = "Hayat: " + cat.lifeSpan + " tahun"
//                binding.tvOriginFragDetail.text = "Asal: ${cat.origin}"
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun poundsToKgConversion(ps: String): String {
        val conversion = 1 / 2.205f
        val parts = ps.split("-").map { s -> s.trim() }
        var kg1: Float
        var kg2: Float
        try {
            kg1 = parts.first().toInt() * conversion
            kg2 = parts[1].toInt() * conversion
        } catch (e: NumberFormatException) {
            kg1 = conversion
            kg2 = conversion
        }
        return "Berat dewasa: %.2f - %.2f kg".format(kg1, kg2)
    }
}