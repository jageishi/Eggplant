package org.ageage.eggplant.login


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import org.ageage.eggplant.BuildConfig
import org.ageage.eggplant.R
import org.ageage.eggplant.common.oauth.HatenaOAuthManager

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }
    private val oAuthManager =
        HatenaOAuthManager(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setupAccessButton()
        setupLoginButton()
    }

    private fun setupAccessButton() {
        buttonAccess.setOnClickListener {
            oAuthManager.fetchAuthorizationUrl()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    },
                    {
                    }
                )
        }
    }

    private fun setupLoginButton() {
        buttonLogin.setOnClickListener {
            oAuthVerifierEditText?.text.toString().let { oAuthVerifier ->
                oAuthManager.fetchAccessToken(oAuthVerifier)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Toast.makeText(requireContext(), "成功", Toast.LENGTH_SHORT).show()
                        },
                        {
                            Toast.makeText(requireContext(), "失敗", Toast.LENGTH_SHORT).show()
                        }
                    )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
