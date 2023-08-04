package tz.co.vanuserve.civilsocieties.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.data.CivilSociety
import tz.co.vanuserve.civilsocieties.data.UserPreferences
import tz.co.vanuserve.civilsocieties.databinding.FragmentHomeBinding
import tz.co.vanuserve.civilsocieties.features.csos.CivilSocietyAdapter
import tz.co.vanuserve.civilsocieties.util.Resource
import tz.co.vanuserve.civilsocieties.util.onQueryTextChanged

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), CivilSocietyAdapter.OnItemClickListener {

    //private lateinit var homeViewModel: CivilSocietyViewModel
    private var mGoogleSignInClientClient: GoogleSignInClient?=null
    protected lateinit var userPreferences:UserPreferences
    private val homeViewModel: CivilSocietyViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)
        val civilSocietyAdapter = CivilSocietyAdapter(this)
        //Initialize Userpreferences
        userPreferences= UserPreferences(requireContext())
        //Add listener to FAB
        binding.addFab.setOnClickListener {
            val userPreferences= UserPreferences(requireContext())

            userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
                 if(it==null) {
                     val action = HomeFragmentDirections.actionNavigationHomeToLoginFragment()
                     findNavController().navigate(action)
                 }else{
                     val action=HomeFragmentDirections.actionNavigationHomeToUploadFragment()
                     findNavController().navigate(action)
                 }
            })

        }

        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClientClient= GoogleSignIn.getClient(requireActivity(),gso)

        binding.apply {
            recyclerView.apply {
                adapter = civilSocietyAdapter
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    reverseLayout = true
                    stackFromEnd = true
                }

            }

            homeViewModel.civilSocieties.observe(viewLifecycleOwner) { result ->
                progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                civilSocietyAdapter.submitList(result.data)

                //textViewError.isVisible=result is Resource.Error && result.data.isNullOrEmpty()
                //textViewError.text=result.error?.localizedMessage
            }


        }
        setHasOptionsMenu(true)

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_home, menu)
        val searchItem = menu.findItem(R.id.homeFragment)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            homeViewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       // return super.onOptionsItemSelected(item)
        when (item.itemId){
            R.id.action_login -> {
               val action=HomeFragmentDirections.actionNavigationHomeToLoginFragment()
                findNavController().navigate(action)
            }

            R.id.action_signup->{
                val action=HomeFragmentDirections.actionNavigationHomeToRegisterFragment()
                findNavController().navigate(action)
            }
            R.id.action_logout->{
                logout()
            }
        }

        //return  true
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(civilSociety: CivilSociety) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailsFragment(civilSociety)
        findNavController().navigate(action)
    }

    fun logout()=lifecycleScope.launch {
        val authToken=userPreferences.authToken.first()
        userPreferences.clear()
        Toast.makeText(requireContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show()

        mGoogleSignInClientClient?.signOut()?.addOnCompleteListener(
            requireActivity(),
            object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    Toast.makeText(requireContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show()
                }
            })
    }


}

