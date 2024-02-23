package com.example.homeworktwentyfive.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.homeworktwentyfive.databinding.FragmentBottomSheetBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var placesClient: PlacesClient

    interface SearchListener {
        fun onOptionSelected(location : LatLng?, title: String?)
    }
    private var listener: SearchListener? = null
    fun setListener(listener: SearchListener) {
        this.listener = listener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), "AIzaSyAVNd8E0CVzbq5EROax3davAXnJbmwFMOs")
        placesClient = Places.createClient(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
    }

    private fun setupSearch() {
        binding.btnSearch.setOnClickListener {
            val query = binding.actvLocationSearch.text.toString()
            if (query.isNotEmpty()) {
                searchLocation(query)
            } else {
                Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun searchLocation(query: String) {
        val token = AutocompleteSessionToken.newInstance()

        val predictionsRequest = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(predictionsRequest)
            .addOnSuccessListener { response ->
                val placeIds = response.autocompletePredictions.map { it.placeId }

              val placeId = placeIds.firstOrNull()
                placeId?.let {
                    fetchPlaceDetails(it) { latLng ->
                        // Use the LatLng for your needs
                        Log.i("PlaceDetails", "Location: $latLng")
                        listener?.onOptionSelected(latLng,query)
                        dismiss()
                    }
                }
            }
            .addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("PlacePrediction", "Place not found: " + exception)
                }
            }
    }

    private fun fetchPlaceDetails(placeId: String, callback: (LatLng?) -> Unit) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { response ->
            val place = response.place
            callback(place.latLng)
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e("PlaceDetails", "Place not found: ${exception}")
                callback(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
