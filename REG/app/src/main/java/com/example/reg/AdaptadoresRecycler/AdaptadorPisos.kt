package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.reg.Objetos.Piso
import com.example.reg.databinding.FilaPisoBinding
import java.util.*

class AdaptadorPisos(val lista:List<Piso>,val contexto:Context):RecyclerView.Adapter<AdaptadorPisos.ViewHolder>(), Filterable {

    var listaFiltrada = lista

    class ViewHolder(val bind: FilaPisoBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaPisoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]
        with(holder.bind){
            pisoCalle.text=l.calle
            pisoBaOs.text=l.nBaths
            pisoHabitaciones.text=l.nHabs
            pisoM2.text=l.m2.toString()
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