package com.example.hqawesomeapp.hqHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hqawesomeapp.HQViewModel
import com.example.hqawesomeapp.R
import com.example.hqawesomeapp.databinding.FragmentItemListBinding

class HQFragment : Fragment(), HQItemListener {

    private lateinit var adapter: HQListAdapter
    private val viewModel by navGraphViewModels<HQViewModel>(R.id.hq_graph){defaultViewModelProviderFactory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentItemListBinding.inflate(inflater)

        val view = binding.root
        val recyclerView = binding.list

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = HQListAdapter(this)

        recyclerView.apply {
            this.adapter = this@HQFragment.adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        initObservers()
        return view
    }

    private fun initObservers(){
        viewModel.hqListLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.updateData(it)
            }
        })

        viewModel.navigationToDetailsLiveData.observe(viewLifecycleOwner, Observer{
                it.getContentIfNotHandled()?.let {
                    val action = HQFragmentDirections.actionHQFragmentToHQDetailsFragment()
                    findNavController().navigate(action)
                }
        })
    }

    override fun onItemSelected(position: Int) {
        viewModel.onHQSelected(position)
    }

}