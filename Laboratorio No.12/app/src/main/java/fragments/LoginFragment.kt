package com.example.architecturecomponents.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.architecturecomponents.R
import com.example.architecturecomponents.databinding.FragmentLoginBinding
import com.example.architecturecomponents.viewModel.SessionViewModel
import kotlinx.coroutines.flow.collectLatest

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.buttonLoginFragmentLogin.setOnClickListener{
            binding.buttonLoginFragmentLogin.isVisible = false
            binding.progressLoginFragment.isVisible = true

            val email = binding.inputTextLoginFragmentEmail.editText!!.text.toString()
            val password = binding.inputTextLoginFragmentPassword.editText!!.text.toString()
            val defaultEmail = getString(R.string.defaultEmail)

            sessionViewModel.login(email, password, defaultEmail)
        }
    }

    private fun setObservers() {
        lifecycleScope.launchWhenStarted {
            sessionViewModel.validAuthToken.collectLatest{
                if(it.auth == true)
                    requireView().findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                else if (it.auth == false && binding.inputTextLoginFragmentEmail.editText!!.text.toString() != "") {
                    Toast.makeText(requireContext(), getString(R.string.errorLogin), Toast.LENGTH_LONG).show()
                    binding.progressLoginFragment.isVisible = false
                    binding.buttonLoginFragmentLogin.isVisible = true
                }
            }
        }
    }

}