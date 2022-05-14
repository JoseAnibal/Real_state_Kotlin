package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.Actividades.Admin
import com.example.reg.Actividades.LoggedUser
import com.example.reg.Actividades.NotLoggedUserVerPiso
import com.example.reg.Objetos.Piso
import com.example.reg.Objetos.Usuario
import com.example.reg.R
import com.example.reg.SharedManager
import com.example.reg.databinding.FilaPisoBinding
import com.example.reg.databinding.FilaUsuariosBinding
import com.example.reg.listReg
import java.util.*

class AdaptadorUsuariosLista(val lista:List<Usuario>, val contexto:Context):RecyclerView.Adapter<AdaptadorUsuariosLista.ViewHolder>(), Filterable {
    val SM by lazy {
        SharedManager(contexto)
    }
    var listaFiltrada = lista
    var tipo=0
    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .fitCenter()

    class ViewHolder(val bind: FilaUsuariosBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaUsuariosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]
        with(holder.bind){
            usuCorreo.text=l.correo
            usuNombre.text=l.nombre
        }
        if(l.resgistrado == true){
            Glide.with(contexto).load(listReg[0]).into(holder.bind.usuRegistrado)
        }else{
            Glide.with(contexto).load(listReg[1]).into(holder.bind.usuRegistrado)
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
                    val listaFiltrada2 = mutableListOf<Usuario>()
                    for (alu in lista) {
                        val nombreMinuscula = alu.correo!!.lowercase(Locale.ROOT)
                        val textoMinuscula = texto.lowercase(Locale.ROOT)
                        if (nombreMinuscula.contains(textoMinuscula)) {
                            listaFiltrada2.add(alu)
                        }
                    }
                    listaFiltrada = listaFiltrada2
                }
                //FILTROS AQUI
                if(tipo==0){
                    listaFiltrada=listaFiltrada.filter { it.resgistrado==false }
                }else{
                    listaFiltrada=listaFiltrada.filter { it.resgistrado==true }
                }

                val filterResults = FilterResults()
                filterResults.values = listaFiltrada
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = results?.values as MutableList<Usuario>
                notifyDataSetChanged()
            }
        }
    }

}