package com.maklumi.cats.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.maklumi.cats.databinding.ItemCatBinding
import com.maklumi.cats.model.sqlpart.Cat
import com.maklumi.cats.util.lukisanPutaran
import com.maklumi.cats.util.muatturun

class CatListAdapter(private val catList: ArrayList<Cat>) :
    RecyclerView.Adapter<CatListAdapter.CatViewHolder>() {

    fun updateCatList(newCatList: List<Cat>) {
        catList.clear()
        catList.addAll(newCatList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.binding.tvNamaItemCat.text = catList[position].name
        holder.binding.tvJangkaHayatItemCat.text = catList[position].lifeSpan
        holder.binding.root.setOnClickListener {
            val catUuid = catList[position].uuid
            it.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailFragment(catUuid))
        }
        holder.binding.ivCatItemCat.muatturun(
            catList[position].image,
            lukisanPutaran(holder.binding.ivCatItemCat.context)
        )
    }

    override fun getItemCount(): Int = catList.size

    class CatViewHolder(val binding: ItemCatBinding) : RecyclerView.ViewHolder(binding.root)
}