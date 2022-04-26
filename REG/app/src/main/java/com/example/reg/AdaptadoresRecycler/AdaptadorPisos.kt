package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reg.Objetos.Piso
import com.example.reg.SharedManager
import com.example.reg.databinding.FilaPisoBinding
import java.util.*

class AdaptadorPisos(val lista:List<Piso>,val contexto:Context):RecyclerView.Adapter<AdaptadorPisos.ViewHolder>(), Filterable {
    val SM by lazy {
        SharedManager(contexto)
    }
    var listaFiltrada = lista

    class ViewHolder(val bind: FilaPisoBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaPisoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]
        with(holder.bind){
            editar.isEnabled=false
            editar.visibility= View.INVISIBLE
            pisoCalle.text=l.calle
            pisoBaOs.text=l.nbaths
            pisoHabitaciones.text=l.nhabs
            pisoM2.text=l.m2.toString()

            if(SM.idUsuario=="admin"){
                editar.isEnabled=true
                editar.visibility= View.VISIBLE
            }

            Glide.with(contexto).load(l.imagenes!![0]).into(holder.bind.pisoImagen)
        }
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(busqueda: CharSequence?): FilterResults {
                val texto = busqueda.toString()
                //Filtro 1, el clásico de por nombre, no hace falta pensar
                if (texto.isEmpty()) {
                    listaFiltrada = lista
                } else {
                    val listaFiltrada2 = mutableListOf<Piso>()
                    for (alu in lista) {
                        val nombreMinuscula = alu.calle!!.lowercase(Locale.ROOT)
                        val textoMinuscula = texto.lowercase(Locale.ROOT)
                        if (nombreMinuscula.contains(textoMinuscula)) {
                            listaFiltrada2.add(alu)
                        }
                    }
                    listaFiltrada = listaFiltrada2
                }
                //FILTROS AQUI

                val filterResults = FilterResults()
                    filterResults.values = listaFiltrada
                    return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = results?.values as MutableList<Piso>
                notifyDataSetChanged()
            }
        }
    }

}