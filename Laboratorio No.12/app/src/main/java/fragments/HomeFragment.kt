package com.example.architecturecomponents.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.architecturecomponents.R
import com.example.architecturecomponents.databinding.FragmentHomeBinding
import com.example.architecturecomponents.viewModel.HomeViewModel
import com.example.architecturecomponents.viewModel.SessionViewModel
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservables()
        setListeners()
    }

    private fun setListeners() {
        binding.buttonHomeFragmentKeepSessionActive.setOnClickListener{
            sessionViewModel.keepSessionActive()
        }
        binding.buttonHomeFragmentLogout.setOnClickListener{
            sessionViewModel.logout()
        }
        binding.buttonHomeFragmentDefault.setOnClickListener {
            homeViewModel.setState(HomeViewModel.State.Default)
        }
        binding.buttonHomeFragmentSuccess.setOnClickListener {
            homeViewModel.setState(HomeViewModel.State.Success)
        }
        binding.buttonHomeFragmentFailure.setOnClickListener {
            homeViewModel.setState(HomeViewModel.State.Failure)
        }
        binding.buttonHomeFragmentEmpty.setOnClickListener {
            homeViewModel.setState(HomeViewModel.State.Empty)
        }
    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted{
            sessionViewModel.validAuthToken.collectLatest {
                if(sessionViewModel.validAuthToken.value.auth == false)
                    requireView().findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            homeViewModel.state.collectLatest {
                when(it){
                    is HomeViewModel.State.Default -> {
                        binding.apply {
                            imageHomeFragmentState.setImageDrawable(
                                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_default)
                            )
                            textViewHomeFragmentState.text = getString(R.string.defaultAlert)
                        }
                    }
                    is HomeViewModel.State.Success -> {
                        binding.apply {
                            imageHomeFragmentState.setImageDrawable(
                                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_success)
                            )
                            textViewHomeFragmentState.text = getString(R.string.successAlert)
                        }
                    }
                    is HomeViewModel.State.Failure -> {
                        binding.apply {
                            imageHomeFragmentState.setImageDrawable(
                                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_failure)
                            )
                            textViewHomeFragmentState.text = getString(R.string.failureAlert)
                        }
                    }
                    is HomeViewModel.State.Empty -> {
                        binding.apply {
                            imageHomeFragmentState.setImageDrawable(
                                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_empty)
                            )
                            textViewHomeFragmentState.text = getString(R.string.emptyAlert)
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            homeViewModel.loading.collectLatest {
                binding.apply {
                    when(it){
                        HomeViewModel.State.Default -> {
                            buttonHomeFragmentDefault.isEnabled = true
                            buttonHomeFragmentSuccess.isEnabled = false
                            buttonHomeFragmentFailure.isEnabled = false
                            buttonHomeFragmentEmpty.isEnabled = false
                            imageHomeFragmentState.isVisible = false
                            textViewHomeFragmentState.isVisible = false
                            progressHomeFragment.isVisible = true
                        }
                        HomeViewModel.State.Success -> {
                            buttonHomeFragmentDefault.isEnabled = false
                            buttonHomeFragmentSuccess.isEnabled = true
                            buttonHomeFragmentFailure.isEnabled = false
                            buttonHomeFragmentEmpty.isEnabled = false
                            imageHomeFragmentState.isVisible = false
                            textViewHomeFragmentState.isVisible = false
                            progressHomeFragment.isVisible = true
                        }
                        HomeViewModel.State.Failure -> {
                            buttonHomeFragmentDefault.isEnabled = false
                            buttonHomeFragmentSuccess.isEnabled = false
                            buttonHomeFragmentFailure.isEnabled = true
                            buttonHomeFragmentEmpty.isEnabled = false
                            imageHomeFragmentState.isVisible = false
                            textViewHomeFragmentState.isVisible = false
                            progressHomeFragment.isVisible = true
                        }
                        HomeViewModel.State.Empty -> {
                            buttonHomeFragmentDefault.isEnabled = false
                            buttonHomeFragmentSuccess.isEnabled = false
                            buttonHomeFragmentFailure.isEnabled = false
                            buttonHomeFragmentEmpty.isEnabled = true
                            imageHomeFragmentState.isVisible = false
                            textViewHomeFragmentState.isVisible = false
                            progressHomeFragment.isVisible = true
                        }
                        else -> {
                            buttonHomeFragmentDefault.isEnabled = true
                            buttonHomeFragmentSuccess.isEnabled = true
                            buttonHomeFragmentFailure.isEnabled = true
                            buttonHomeFragmentEmpty.isEnabled = true
                            imageHomeFragmentState.isVisible = true
                            textViewHomeFragmentState.isVisible = true
                            progressHomeFragment.isVisible = false
                        }
                    }
                }
            }
        }
    }
}