package com.example.virtualrunner.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.virtualrunner.R
import com.example.virtualrunner.Run
import com.example.virtualrunner.databinding.FragmentRunBinding
import feri.pora.volunteerhub.SharedViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class RunFragment : Fragment() {
    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var map: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunBinding.inflate(inflater, container, false)
        val rootView = binding.root
        map = binding.map
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val run = sharedViewModel._selectedRun.value
        if (run != null) {
            binding.nameView.text = run.name
        }
        if (run != null) {
            binding.dateView.text = run.date
        }
        if (run != null) {
            binding.timeView.text = run.time
        }
        if (run != null) {
            binding.distanceView.text = run.distance.toString()
        }
        if (run != null) {
            binding.elevationView.text = run.elevation.toString()
        }

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setCenter(GeoPoint(46.5547, 15.6459))
        mapController.setZoom(15)

//        val marker = Marker(map)
//        marker.position = GeoPoint(args.argLat.toDouble(), args.argLng.toDouble())
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        val drawable: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.location)!!
//        marker.icon = drawable
//        map.overlays.add(marker)
//        map.invalidate()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
