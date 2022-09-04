package tz.co.vanuserve.civilsocieties.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import tz.co.vanuserve.civilsocieties.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}