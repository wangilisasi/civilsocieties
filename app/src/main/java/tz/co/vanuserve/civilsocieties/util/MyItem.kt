package tz.co.vanuserve.civilsocieties.util

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyItem(
    private val lat:Double,
    private val lng:Double,
    private val title:String?,
    private val snippet:String?
):ClusterItem {
    private  var mPosition: LatLng= LatLng(lat,lng)
    private var mTitle: String? = title
    private var mSnippet: String? = snippet

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String? {
        return mTitle
    }

    override fun getSnippet(): String? {
        return mSnippet
    }

    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    fun setTitle(title: String?) {
        mTitle = title
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    fun setSnippet(snippet: String?) {
        mSnippet = snippet
    }
}