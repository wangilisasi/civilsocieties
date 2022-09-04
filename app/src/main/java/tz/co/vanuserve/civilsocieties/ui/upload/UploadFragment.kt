package tz.co.vanuserve.civilsocieties.ui.upload

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.AndroidEntryPoint
import tz.co.vanuserve.civilsocieties.R
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.databinding.FragmentUploadBinding
import tz.co.vanuserve.civilsocieties.util.FileUtils
import tz.co.vanuserve.civilsocieties.util.visible
import java.io.File

@AndroidEntryPoint
class UploadFragment: Fragment(R.layout.fragment_upload) {
    private val IMAGE_PICK_CODE = 1000
    private val LOCATION_CODE = 1001
    private  val TAG = "UploadFragment"
    var filepath:String?=null
    var imageUri: Uri?=null
    var latitude: String? = null
    var longitude: String? = null

    var locationManager: LocationManager? = null
    private lateinit var locationListener: LocationListener
    private var fileUtils:FileUtils= FileUtils()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val uploadViewModel: UploadViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding=FragmentUploadBinding.bind(view)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )
        setUpAutoCompleteTextViews(binding)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Sending....")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        binding.btnSubmit.setOnClickListener{
            getValues(binding)
            progressDialog.show()
        }

        binding.btnGetCoordinates.setOnClickListener{
            //Check Runtime Permission
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
                    getLastLocation(binding)
                }
            } else {
                getLastLocation(binding)
            }
        }

        uploadViewModel.uploadResponse.observe(viewLifecycleOwner, Observer {
            //binding.progressBarFill.visible(it is Resource.Loading)  //Hide progressbar
            when (it) {
                is Resource.Success -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    val action=UploadFragmentDirections.actionUploadFragmentToNavigationHome()
                    findNavController().navigate(action)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }  //pass login function as a parameter
            }
        })


        binding.uploadImage.setOnClickListener{

            //Check Runtime Permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {

                    // Requesting the permission
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        IMAGE_PICK_CODE
                    )
                } else {
                    pickImageFromGallery()
                }
            } else {
                //Android version was less than MarshMallow
                pickImageFromGallery()
            }
        }

    }

    private fun setUpAutoCompleteTextViews(binding: FragmentUploadBinding) {
        //Populate the country auto complete
        val autoCompleteCountry= binding.country
        val countries=resources.getStringArray(R.array.Countries)
        val adapterCountries=ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1,
            countries
        )  //In Fragment class, I use activity as Context. Otherwise there is type mismatch error in Kotlin.
        autoCompleteCountry.setAdapter(adapterCountries)

        //Populate Region autocomplete
        val autoCompleteRegions=binding.yourRegion
        val regions=resources.getStringArray(R.array.Regions)
        val adapterRegions=ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1,
            regions
        )
        autoCompleteRegions.setAdapter(adapterRegions)

        val autoCompleteType=binding.csoType
        val type=resources.getStringArray(R.array.Type)
        val adapterTypes=ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1,
            type
        )
        autoCompleteType
            .setAdapter(adapterTypes)
    }

    private fun getValues(binding: FragmentUploadBinding){
        binding.apply {
            val name=csoFillName.text.toString()
            val description=csoDescFill.text.toString()
            val region=yourRegion.text.toString()
            val file: File = File(fileUtils.getPath(imageUri!!, requireContext()))
            uploadViewModel.uploadDetails(name, description, region, latitude, longitude, file)
        }

    }

    fun getLastLocation(binding: FragmentUploadBinding) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener(requireActivity(),
                    OnSuccessListener<Location?> { location -> // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            binding.currentLocation.setText(
                                String.format(
                                    "%.5f",
                                    location.latitude
                                ) + "," + String.format("%.5f", location.longitude)
                            )
                            latitude = String.format("%.8f", location.latitude)
                            longitude = String.format("%.8f", location.longitude)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please allow location services on your device settings",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, null)
        intent.type = "image/*"
       startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //Handle Result of runtime permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            IMAGE_PICK_CODE -> {
                run {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickImageFromGallery()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Storage Permission was Denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                run {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                0,
                                10f,
                                locationListener
                            )
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Location Permission was Denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            LOCATION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            10f,
                            locationListener
                        )
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location Permission was Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    //Handle result of picked Image
    //Handle result of picked Image
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
             val imageToUpload: ImageView=requireActivity().findViewById(R.id.upload_image)
            imageToUpload.setImageURI(data!!.data)
            filepath = data.data!!.path
            imageUri = data.data
            Log.d(TAG, "onActivityResult: $imageUri")
        }
    }

}