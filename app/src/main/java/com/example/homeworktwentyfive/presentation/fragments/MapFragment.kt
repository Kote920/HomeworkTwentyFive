package com.example.homeworktwentyfive.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.homeworktewentytwo.data.common.Resource
import com.example.homeworktewentytwo.presentation.base.BaseFragment
import com.example.homeworktwentyfive.databinding.FragmentMapBinding
import com.example.homeworktwentyfive.presentation.viewModels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null


    override fun setUp() {
        mapView = binding.mapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

    }

    override fun listeners() {
        binding.btnCurrentLocation.setOnClickListener {
            if (checkPermission()) {
                viewModel.getLocation()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        bindObserves()

        binding.btnSearch.setOnClickListener {
            showBottomSheet()
        }
    }

     private fun changeMark(currentLocation: LatLng, title: String){
        googleMap?.addMarker(MarkerOptions().position(currentLocation).title(title))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationFlow.collect() {
                    when(it){
                        is Resource.Loading -> {}
                        is Resource.Success -> changeMark(it.responseData, "Current Location")
                        is Resource.Failed -> Toast.makeText(
                            requireContext(),
                            "Error!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.getLocation()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }


    private fun showBottomSheet() {
        val bottomSheetFragment = BottomSheetFragment().apply {
            setListener(object : BottomSheetFragment.SearchListener {
                override fun onOptionSelected(location: LatLng?, title: String?) {
                    d("locationbla", location.toString())
                    if(location != null){
                        changeMark(location, title?:"")
                    }else{
                        Toast.makeText(requireContext(), "Location Not Found!", Toast.LENGTH_SHORT).show()
                    }

                }
            })
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }


}
