package com.palchak.sergey.stateflow

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.palchak.sergey.stateflow.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect {
                when (it) {
                    is MainViewModel.LoginUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Successfully logged in",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is MainViewModel.LoginUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            it.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is MainViewModel.LoginUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is MainViewModel.LoginUiState.Empty -> Unit
                }
            }
        }
    }
}