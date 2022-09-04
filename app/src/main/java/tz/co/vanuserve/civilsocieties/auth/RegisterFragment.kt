package tz.co.vanuserve.civilsocieties.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.databinding.FragmentRegisterBinding

import tz.co.vanuserve.civilsocieties.util.visible

@AndroidEntryPoint
class RegisterFragment: Fragment(R.layout.fragment_register) {

    private val registerViewModel:RegisterViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding=FragmentRegisterBinding.bind(view)
        binding.buttonRegister.setOnClickListener{
            register(binding)
        }

        registerViewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    val action = RegisterFragmentDirections.actionRegisterFragmentToUploadFragment()
                    findNavController().navigate(action)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun register(binding: FragmentRegisterBinding){
        val email=binding.editTextTextEmailAddress.text.toString().trim()
        val password=binding.editTextTextPassword.text.toString().trim()
        registerViewModel.register(email,password)
    }
}