package com.maklumi.cats.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.maklumi.cats.databinding.FragmentDetailBinding
import com.maklumi.cats.util.WarnaLatar
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
                it.image?.let { url -> tukarWarnaLatar(url) }
                binding.tvWeightFragDetail.text = poundsToKgConversion(cat.weight)
            }
        }
    }

    private fun tukarWarnaLatar(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate { palette ->
                        val intColor = palette?.vibrantSwatch?.rgb ?: 0
                        val myPalette = WarnaLatar(intColor)
                        binding.warnapelet = myPalette
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
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