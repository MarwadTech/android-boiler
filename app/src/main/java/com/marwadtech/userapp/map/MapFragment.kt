package com.marwadtech.userapp.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.marwadtech.userapp.R
import com.marwadtech.userapp.databinding.FragmentMapBinding
import com.marwadtech.userapp.retrofit.models.customModels.MapAddressModel
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap
    private var currentMarker: Marker? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize Places API client
        Places.initialize(requireActivity(), getString(R.string.google_maps_api_key))
        placesClient = Places.createClient(requireContext())
        binding.btnSaveNow.isEnabled = false
    }

    companion object {
        private val TAG = FragmentMapBinding::class.java.name
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Enable my location layer
            googleMap.isMyLocationEnabled = true

            // Move camera to current location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f))
                }
            }
        } else {
            // Request location permission
            // You need to handle the permission request and callback
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun addMarker(latLng: LatLng) :Marker?{
        // Add a marker on the map at the tapped location
        return googleMap.addMarker(MarkerOptions().position(latLng).title("Tapped Location"))
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set up initial map settings
        val initialLatLng = LatLng(37.7749, -122.4194) // Default to San Francisco
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 12.0f))

        // Center on current location
        enableMyLocation()

        // Set up map click listener
        googleMap.setOnMapClickListener { latLng ->
            // Handle map tap event
            Log.e(TAG, "onMapReady: $latLng", )
            currentMarker?.remove()
            currentMarker = addMarker(latLng)
            fetchLocation(latLng)
//            fetchShopName(latLng)
        }
    }

    private fun fetchLocation(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0]
                Log.e(TAG, "fetchLocation: $addresses", )
//                Log.e(
//                    TAG,
//                    "setMarkerLatestPosition: " +
//                            "locality : ${address.locality} \n" +
//                            "postalCode : ${address.postalCode} \n" +
//                            "extras : ${address.extras} \n" +
//                            "featureName : ${address.featureName} \n" +
//                            "premises : ${address.premises} \n" +
//                            "subLocality : ${address.subLocality} \n" +
//                            "subAdminArea : ${address.subAdminArea} \n" +
//                            "adminArea : ${address.adminArea} \n" +
//                            "countryCode : ${address.countryCode} \n" +
//                            "countryName : ${address.countryName} \n" +
//                            "latitude : ${address.latitude} \n" +
//                            "locale : ${address.locale} \n" +
//                            "longitude : ${address.longitude} \n" +
//                            "phone : ${address.phone} \n" +
//                            "subThoroughfare : ${address.subThoroughfare} \n" +
//                            "thoroughfare : ${address.thoroughfare} \n" +
//                            "url : ${address.url} \n" +
//                            "subLocality : ${address.subLocality}"
//                )
                setLocationData(address)
            }
        } catch (e: IOException) {
            Log.e("MapTap", "Error during reverse geocoding: ${e.message}")
        }
    }
//    private fun fetchShopName(latLng: LatLng) {
//        // Set the fields to specify the types of place data to return
//        val placeFields = listOf(Place.Field.NAME)
//
//        // Use the builder to create a FindCurrentPlaceRequest
//        val request = FetchPlaceRequest.newInstance(
//            listOf("place_id_placeholder").toString(), // A placeholder for the place ID, will be replaced in the callback
//            placeFields
//        )
//
//        // Use the PlacesClient to fetch the shop name
//        placesClient.fetchPlace(request).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val response = task.result
//                val place = response?.place
//
//                // Log the shop name
//                val shopName = place?.name
//                Log.d("MapTap", "Shop Name: $shopName")
//            } else {
//                val exception = task.exception
//                Log.e("MapTap", "Error fetching shop name: ${exception?.message}")
//            }
//        }
//    }

    private fun setLocationData(address: Address) {
        binding.txtCity.text = "${address.getAddressLine(0)}"
        binding.btnSaveNow.isEnabled = true
        binding.btnSaveNow.setSingleClickListener {
            val directions = MapFragmentDirections.actionMapFragmentToDeliveryAddressFragment(
                mapFetchData = MapAddressModel(
                    addressLine = address.getAddressLine(0),
                    city = address.locality,
                    state = address.adminArea,
                    pinCode = address.postalCode,
                ),
                addressData = null
            )
            findNavController().navigate(directions)
        }
    }

}