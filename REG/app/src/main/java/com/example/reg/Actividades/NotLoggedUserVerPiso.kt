package com.example.reg.Actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.AdaptadoresRecycler.AdaptadorFotosPiso
import com.example.reg.Objetos.Piso
import com.example.reg.R
import com.example.reg.databinding.ActivityNotLoggedUserVerPisoBinding

class NotLoggedUserVerPiso : AppCompatActivity() {
    lateinit var AdaptadorFotosPiso:AdaptadorFotosPiso
    private lateinit var binding: ActivityNotLoggedUserVerPisoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNotLoggedUserVerPisoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val bundle = intent.extras
        val objeto = bundle?.getParcelable("Piso")?: Piso()

        binding.pisoBaths.text=objeto.nbaths
        binding.pisoCalle.text=objeto.calle
        binding.pisoHabs.text=objeto.nhabs
        binding.pisoM2.text=objeto.m2.toString()
        binding.pisoDescripcion.text=objeto.descripcion
        binding.pisoPrecio.text=objeto.precio.toString()

        binding.rvFotosPiso.adapter=AdaptadorFotosPiso(objeto.imagenes!!.toMutableList(),this)
        binding.rvFotosPiso.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
    }
}