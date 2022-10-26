package org.ageage.eggplant.login


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.ageage.eggplant.R

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }
    private var loginListener: OnLoginListener? = null

    interface OnLoginListener {
        fun onLogin()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnLoginListener) {
            loginListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
    }

    override fun onDetach() {
        super.onDetach()

        loginListener = null
    }

    private fun initViewModel() {
        viewModel.statusFetchAuthorizationUrl.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoginViewModel.Status.Loading -> {
                }
                is LoginViewModel.Status.Success -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.data)))
                }
                is LoginViewModel.Status.Error -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_to_link_with_hatena),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.statusLogin.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoginViewModel.Status.Loading -> {
                }
                is LoginViewModel.Status.Success -> {
                    loginListener?.onLogin()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.succeeded_to_login),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                is LoginViewModel.Status.Error -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_to_login),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun initViews() {
        setupAccessButton()
        setupLoginButton()
    }

    private fun setupAccessButton() {
        buttonAccess.setOnClickListener {
            viewModel.fetchAuthorizationUrl()
        }
    }

    private fun setupLoginButton() {
        buttonLogin.setOnClickListener {
            oAuthVerifierEditText?.text.toString().let { oAuthVerifier ->
                viewModel.login(oAuthVerifier)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
