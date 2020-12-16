package com.maklumi.cats.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maklumi.cats.databinding.FragmentListBinding
import com.maklumi.cats.viewmodel.ListViewModel

class ListFragment : Fragment() {

    private val viewModel: ListViewModel by viewModels()
    private val catListAdapter = CatListAdapter(arrayListOf())

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refresh()
        binding.recyclerviewFragList.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewFragList.adapter = catListAdapter

        binding.swipeRefreshLayoutFragList.setOnRefreshListener {
            binding.recyclerviewFragList.visibility = View.GONE
            binding.textViewMesejRalat.visibility = View.GONE
            binding.progressBarUntukLoading.visibility = View.VISIBLE
            viewModel.refresh()
            binding.swipeRefreshLayoutFragList.isRefreshing = false
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cats.observe(viewLifecycleOwner) { cats ->
            cats?.let {
                binding.recyclerviewFragList.visibility = View.VISIBLE
                catListAdapter.updateCatList(cats)
            }
        }

        viewModel.loadingError.observe(viewLifecycleOwner) { isError ->
            isError?.let {
                binding.textViewMesejRalat.visibility = if (isError) View.VISIBLE else View.GONE
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.progressBarUntukLoading.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    binding.recyclerviewFragList.visibility = View.GONE
                    binding.textViewMesejRalat.visibility = View.GONE
                }
            }
        }
    }
}