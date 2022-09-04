package tz.co.vanuserve.civilsocieties.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.data.CivilSociety
import tz.co.vanuserve.civilsocieties.util.MyItem
import java.util.*

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {
    private val TAG = "MapsFragment"
    private val LOCATION_CODE = 102;
    private var mClusterManager: ClusterManager<MyItem>? = null

    private val mLocationManager: LocationManager? = null
    //private lateinit var dashboardViewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private val mapsViewModel: MapsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        }
        setHasOptionsMenu(true)
    }

//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        dashboardViewModel =
//                ViewModelProvider(this).get(MapsViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_maps, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
        mMap.uiSettings.isZoomControlsEnabled = true
        setBlueDot(mMap)
        var csos: List<CivilSociety>?

        //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

        //Clustering
        mClusterManager = ClusterManager(requireContext(), mMap)
        mMap.setOnCameraIdleListener(mClusterManager)

        mapsViewModel.civilSocieties.observe(viewLifecycleOwner) { result ->
            csos = result.data
           // mMap.clear()
            mapsViewModel.civilSocieties.removeObservers(viewLifecycleOwner)
            val myItems: MutableList<MyItem> = ArrayList()
            csos!!.forEach {
                val sydney = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                //mMap.addMarker(MarkerOptions().position(sydney).title(it.name))
               myItems.add(
                   MyItem(
                       it.latitude.toDouble(),
                       it.longitude.toDouble(),
                       it.name,
                       it.description
                   )
               )
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }


            // Add a custom InfoWindowAdapter by setting it to the MarkerManager.Collection object from
            // ClusterManager rather than from GoogleMap.setInfoWindowAdapter
            mClusterManager!!.markerCollection.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoWindow(marker: Marker): View {
                    val inflater = LayoutInflater.from(requireContext())
                    val view: View = inflater.inflate(R.layout.custom_info_window, null)
                    val textView = view.findViewById<TextView>(R.id.textViewTitle)
                    val textSnippet=view.findViewById<TextView>(R.id.textViewSnippet)
                    val text = if (marker.title != null) marker.title else "Cluster Item"
                    val text1=if (marker.snippet != null) marker.snippet else "Cluster Item"
                    textView.text = text
                    textSnippet.text=text1
                    return view
                }

                override fun getInfoContents(marker: Marker): View? {
                    return null
                }
            })
            mClusterManager!!.addItems(myItems)

            zoomToCurrentLocation(mMap)
        }

    }

    private fun setBlueDot(googleMap: GoogleMap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                // Requesting the permission
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_CODE
                )
            } else {
                googleMap.isMyLocationEnabled = true
            }
        } else {
            googleMap.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //Toast.makeText(getActivity(),"Location Permission Granted Successfully",Toast.LENGTH_SHORT).show();
                        setBlueDot(mMap)
                    }
                } else {
                    Toast.makeText(activity, "Location Permission was Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun zoomToCurrentLocation(googleMap: GoogleMap) {
        var fusedLocationProviderClient: FusedLocationProviderClient? = null
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(
                    requireActivity(),
                    OnSuccessListener<Location?> { location -> // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    ), 4.5f
                                )
                            )
                        } else {
                            Toast.makeText(activity, "Location is null", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // return super.onOptionsItemSelected(item)
        when (item.itemId){
            R.id.action_login_map -> {
                val action = MapsFragmentDirections.actionNavigationDashboardToLoginFragment()
                findNavController().navigate(action)
            }

            R.id.action_signup_map -> {
                val action = MapsFragmentDirections.actionNavigationDashboardToRegisterFragment()
                findNavController().navigate(action)
            }
        }

        //return  true
        return super.onOptionsItemSelected(item)
    }
}