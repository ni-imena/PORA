package com.example.virtualrunner.fragments

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
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
import org.osmdroid.views.overlay.Polyline
import java.util.Locale

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
            val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
            val date = originalFormat.parse(run.date)
            binding.dateView.text = targetFormat.format(date)
        }
        if (run != null) {
            binding.timeView.text = run.time
        }
        if (run != null) {
            binding.distanceView.text = run.distance.toString() + " m"
        }
        if (run != null) {
            binding.elevationView.text = run.elevation.toString() + " m"
        }

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15)

        val points = run?.latlng?.data?.map { GeoPoint(it[0], it[1]) }

        if (points != null) {
            val latitudes = points.map { it.latitude }
            val longitudes = points.map { it.longitude }

            val minLat = latitudes.minOrNull() ?: 0.0
            val maxLat = latitudes.maxOrNull() ?: 0.0
            val minLon = longitudes.minOrNull() ?: 0.0
            val maxLon = longitudes.maxOrNull() ?: 0.0

            val midLat = (minLat + maxLat) / 2
            val midLon = (minLon + maxLon) / 2

            mapController.setCenter(GeoPoint(midLat, midLon))
        }

        val line = Polyline()
        line.setPoints(points)
        line.color = Color.BLACK
        map.overlays.add(line)
        map.invalidate()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
