package org.ageage.eggplant.settings


import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.ageage.eggplant.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findPreference("ossLicenses")?.setOnPreferenceClickListener {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            false
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}
