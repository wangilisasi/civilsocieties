package tz.co.vanuserve.civilsocieties.util

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