package tz.co.vanuserve.civilsocieties.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.databinding.FragmentHomeBinding
import tz.co.vanuserve.civilsocieties.features.csos.CivilSocietyAdapter
import tz.co.vanuserve.civilsocieties.util.Resource

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    //private lateinit var homeViewModel: CivilSocietyViewModel
    private val homeViewModel: CivilSocietyViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)
        val civilSocietyAdapter = CivilSocietyAdapter()
        binding.apply {
            recyclerView.apply {
                adapter = civilSocietyAdapter
                layoutManager = LinearLayoutManager(requireContext()).apply{
                    reverseLayout=true
                    stackFromEnd=true
                }

            }

            homeViewModel.civilSocieties.observe(viewLifecycleOwner) { result ->
                civilSocietyAdapter.submitList(result.data)

                progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                //textViewError.isVisible=result is Resource.Error && result.data.isNullOrEmpty()
                //textViewError.text=result.error?.localizedMessage
            }


        }
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_home,menu)
        val searchItem=menu.findItem(R.id.action_search)
        val searchView=searchItem.actionView as SearchView

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}

