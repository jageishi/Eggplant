package org.ageage.eggplant.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.ageage.eggplant.BuildConfig
import org.ageage.eggplant.R
import org.ageage.eggplant.common.oauth.HatenaOAuthManager

private const val KEY_OAUTH_VERIFIER = "oauth_verifier"

class LoginActivity : AppCompatActivity() {

    private val oAuthManager =
        HatenaOAuthManager(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            if (it.action == Intent.ACTION_VIEW) {
                it.data?.let { uri ->
                    uri.getQueryParameter(KEY_OAUTH_VERIFIER)?.let { oAuthVerifier ->
                        oAuthManager.fetchAccessToken(oAuthVerifier)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show()
                                },
                                {
                                    Toast.makeText(this, "失敗", Toast.LENGTH_SHORT).show()
                                }
                            )
                    }
                }
            }
        }
    }

    private fun initViews() {
        setupActionBar()
        setupLoginButton()
    }

    private fun setupActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.title_login_activity)
        }
    }

    private fun setupLoginButton() {
        buttonLogin.setOnClickListener {
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
}
