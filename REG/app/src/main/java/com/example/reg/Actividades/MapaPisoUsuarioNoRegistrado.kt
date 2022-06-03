package com.example.reg.Actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reg.Objetos.Piso
import com.example.reg.R
import com.example.reg.databinding.ActivityMainBinding
import com.example.reg.databinding.ActivityMapaPisoUsuarioNoRegistradoBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaPisoUsuarioNoRegistrado : AppCompatActivity(), OnMapReadyCallback {
    lateinit var map:GoogleMap
    var objeto= mutableListOf<String>()
    var calle=""

    lateinit var binding: ActivityMapaPisoUsuarioNoRegistradoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaPisoUsuarioNoRegistradoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFragment()
    }

    override fun onStart() {
        super.onStart()
        //PARA RECIBIRLO
        val bundle = intent.extras
        val objecto = bundle?.getParcelable<Piso>("Coords")?:Piso()
        objeto=objecto.coordenadas!!.split(",").toMutableList()
        calle=objecto.calle.toString()
    }

    fun createFragment(){
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapitaUsu) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        createMarker()
    }

    fun createMarker(){
        val coordinates= LatLng(objeto[0].toDouble(),objeto[1].toDouble())
        val marker= MarkerOptions().position(coordinates).title(calle).icon(BitmapDescriptorFactory.fromResource(R.drawable.casalogo))
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            1000,
            null
        )
    }

}