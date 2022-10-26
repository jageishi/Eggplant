package org.ageage.eggplant.login

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import org.ageage.eggplant.R

class LoginActivity : AppCompatActivity(), LoginFragment.OnLoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupActionBar()
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, LoginFragment.newInstance())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLogin() {
        finish()
    }

    private fun setupActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.title_login_activity)
        }
    }
}
