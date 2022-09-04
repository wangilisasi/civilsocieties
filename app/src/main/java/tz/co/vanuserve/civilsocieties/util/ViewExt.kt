package tz.co.vanuserve.civilsocieties.util

import android.view.View
import androidx.appcompat.widget.SearchView

inline fun SearchView.onQueryTextChanged(crossinline listener:(String)->Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true  //We return true because we don't want to do anything
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())  //Inptu can be null. The function returns empty string f the newText is null
            return true
        }
    })
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled

    alpha = if (enabled) 1f else 0.5f   //semi trasparent
}