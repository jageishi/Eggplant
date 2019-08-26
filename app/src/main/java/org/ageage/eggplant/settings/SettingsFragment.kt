package org.ageage.eggplant.settings


import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.ageage.eggplant.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPreferences()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    private fun initPreferences() {

        findPreference("currentAppVersion")?.summary =
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName

        findPreference("ossLicenses")?.setOnPreferenceClickListener {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}
