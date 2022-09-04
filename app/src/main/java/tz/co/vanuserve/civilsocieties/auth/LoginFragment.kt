package tz.co.vanuserve.civilsocieties.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.data.UserPreferences
import tz.co.vanuserve.civilsocieties.databinding.FragmentLoginBinding
import tz.co.vanuserve.civilsocieties.ui.home.HomeFragmentDirections
import tz.co.vanuserve.civilsocieties.util.enable
import tz.co.vanuserve.civilsocieties.util.visible

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private val RC_SIGN_IN = 1
    private var mGoogleSigInClient: GoogleSignInClient? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var preferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
        binding.textViewRegisterNow.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        //Initialize preferences class
        preferences = UserPreferences(requireContext())

        //Observer LiveData
        loginViewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        loginViewModel.saveAuthToken(it.value.token!!.token_id, preferences)
                        val action = LoginFragmentDirections.actionLoginFragmentToUploadFragment()
                        findNavController().navigate(action)
                        loginViewModel.loginResponse.removeObservers(viewLifecycleOwner)
                    }
                    Toast.makeText(requireContext(), "Sucess", Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.editTextTextPassword.addTextChangedListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            binding.buttonLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.buttonLogin.setOnClickListener {
            login(binding)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSigInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.googleSigIn.setOnClickListener {
            //Toast.makeText(requireContext(), "The quick brown fox", Toast.LENGTH_SHORT).show()
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())

            if (account != null) {
                val action = LoginFragmentDirections.actionLoginFragmentToUploadFragment()
                findNavController().navigate(action)
            } else {
                binding.googleSigIn.setOnClickListener {
                    signIn()
                }
            }

        }
    }

        private fun login(binding: FragmentLoginBinding) {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()
            loginViewModel.login(email, password)
        }


        private fun signIn() {
            val intent = mGoogleSigInClient!!.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    val action = LoginFragmentDirections.actionLoginFragmentToUploadFragment()
                    findNavController().navigate(action)
                } catch (e: ApiException) {
                    Log.e("TAG", "signInResult:failed code=" + e.statusCode)
                }
            }
        }
}
