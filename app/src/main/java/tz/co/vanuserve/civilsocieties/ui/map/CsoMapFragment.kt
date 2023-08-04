package tz.co.vanuserve.civilsocieties.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tz.co.vanuserve.civilsocieties.R

class CsoMapFragment(private val latitude: String?, private val longitude: String?,private val csoName:String?,private val csoDesc:String?) : Fragment(), OnMapReadyCallback {
    val LOCATION_CODE = 203
    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val v: View = inflater.inflate(R.layout.cso_map_fragment, container, false)

        val supportMapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.google_map) as WorkAroundMapFragment?

        supportMapFragment!!.getMapAsync(this)
        return v

    }


    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        mMap!!.uiSettings.isZoomControlsEnabled=true
        val scrollView: ScrollView=requireActivity().findViewById(R.id.scroll_parent)
        (childFragmentManager.findFragmentById(R.id.google_map) as WorkAroundMapFragment?)
            ?.setListener(object : WorkAroundMapFragment.OnTouchListener {
                override fun onTouch() {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                }
            })

        val sydney = LatLng(latitude!!.toDouble(), longitude!!.toDouble())
        mMap!!.addMarker(
            MarkerOptions().position(sydney).title(csoName)
                .snippet(csoDesc)
        )
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f))

    }
}