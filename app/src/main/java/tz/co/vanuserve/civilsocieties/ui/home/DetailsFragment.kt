package tz.co.vanuserve.civilsocieties.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.databinding.FragmentDetailsBinding
import tz.co.vanuserve.civilsocieties.ui.map.CsoMapFragment

@AndroidEntryPoint
class DetailsFragment: Fragment(R.layout.fragment_details) {

    //Get a handle to the CSO Object sent from the home fragment
    private val args by navArgs<DetailsFragmentArgs>()  // Using delegate
    private var map:GoogleMap? = null
    private lateinit var mapView:MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cso=args.cso
        val binding=FragmentDetailsBinding.bind(view)
        childFragmentManager.beginTransaction().replace(R.id.map_view, CsoMapFragment(cso.latitude,cso.longitude,cso.name,cso.description))
            .commit()
//        mapView = view.findViewById(R.id.map_view)
//        mapView.apply {
//            onCreate(savedInstanceState)
//            getMapAsync{
//                map = it
//            }
//        }
        binding.apply {
            Glide.with(this@DetailsFragment)
                .load(cso.avatar)
                .centerCrop()
                .into(binding.csoSingleImage)
            csoName.text=cso.name
            csoDescription.text=cso.description
        }

        binding.phoneNumber.setOnClickListener{
           val  callIntent: Intent =Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", binding.phoneNumber.text.toString(),null))
            startActivity(callIntent)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // return super.onOptionsItemSelected(item)
        when (item.itemId){
            R.id.action_login_details -> {
                val action=DetailsFragmentDirections.actionDetailsFragmentToLoginFragment()
                findNavController().navigate(action)
            }

            R.id.action_signup_details->{
                val action=DetailsFragmentDirections.actionDetailsFragmentToRegisterFragment()
                findNavController().navigate(action)
            }
        }

        //return  true
        return super.onOptionsItemSelected(item)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mapView?.onSaveInstanceState(outState)
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        mapView?.onResume()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mapView?.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mapView?.onStop()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapView?.onPause()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView?.onLowMemory()
//    }
}