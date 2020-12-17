package com.maklumi.cats.view

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.maklumi.cats.R
import com.maklumi.cats.databinding.DialogHantarSmsBinding
import com.maklumi.cats.databinding.FragmentDetailBinding
import com.maklumi.cats.model.Kucing
import com.maklumi.cats.model.sqlpart.Cat
import com.maklumi.cats.util.MaklumatSMS
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

    private var hantarSms = false
    private var kucingSekarang: Cat? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetch(args.catUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cat.observe(viewLifecycleOwner) { cat ->
            kucingSekarang = cat
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_untuk_detail_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aksi_hantar_sms -> {
                hantarSms = true
                (activity as MainActivity).periksaKebenaranUntukSMS()
            }
            R.id.aksi_kongsi -> {
                kongsiMaklumatKucing()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun bilaKeizinanDiterima(lulus: Boolean) {
        if (hantarSms && lulus) {
            context?.let {
                val infoSms = MaklumatSMS(
                    "",
                    "Menghantar ${kucingSekarang?.name} dari ${kucingSekarang?.origin}",
                    kucingSekarang?.image
                )

                val dialogUntukSms = DialogHantarSmsBinding.inflate(layoutInflater, null, false)
                dialogUntukSms.maklumat = infoSms

                AlertDialog.Builder(it)
                    .setView(dialogUntukSms.root)
                    .setPositiveButton("Hantar SMS") { _, _ ->
                        if (!dialogUntukSms.hantarKepada.text.isNullOrEmpty()) {
                            infoSms.kepada = dialogUntukSms.hantarKepada.text.toString()
                            hantarSMS(infoSms)
                        }
                    }
                    .setNegativeButton("Batal") { _, _ -> }
                    .show()
            }
        }
    }

    private fun hantarSMS(maklumatSMS: MaklumatSMS) {
        SmsManager.getDefault().sendTextMessage(
            maklumatSMS.kepada,
            null,
            maklumatSMS.mesej,
            PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0),
            null
        )
    }

    private fun kongsiMaklumatKucing() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Mau tau pasal kucing ni?")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Menghantar ${kucingSekarang?.name} dari ${kucingSekarang?.origin}"
        )
        intent.putExtra(Intent.EXTRA_STREAM, kucingSekarang?.image)

        startActivity(Intent.createChooser(intent, "Kongsi dengan"))

    }
}