package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.FilaUsuariosBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AdaptadorUsuariosNoRegistrados(val lista:List<Usuario>, val contexto: Context): RecyclerView.Adapter<AdaptadorUsuariosNoRegistrados.ViewHolder>(),
    Filterable {
    val SM by lazy {
        SharedManager(contexto)
    }
    var listaFiltrada = lista
    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .circleCrop()

    class ViewHolder(val bind: FilaUsuariosBinding): RecyclerView.ViewHolder(bind.root)

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
        Glide.with(contexto).load(l.imagen).apply(options).into(holder.bind.usuImagen)
        if(l.resgistrado == true){
            Glide.with(contexto).load(listReg[0]).into(holder.bind.usuRegistrado)
        }else{
            Glide.with(contexto).load(listReg[1]).into(holder.bind.usuRegistrado)
        }

        holder.bind.usuRechazar.setOnClickListener {
            db_ref.child(inmobiliaria).child(usuariosBD).child(l.id.toString()).removeValue()
            Snackbar.make(it, "Usuario rechazado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        holder.bind.usuAceptar.setOnClickListener {
            insertoUsuarioBDReg(l.id!!,l.correo!!,l.nombre!!,l.password!!,l.tipo!!,l.imagen!!,true)
            Snackbar.make(it, "Usuario aceptado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    fun insertoUsuarioBDReg(id:String,correo:String,nombre:String,password:String,tipo:Int,imagen:String,reg:Boolean){
        val crearCuenta= Usuario(id,correo,nombre, password, tipo,imagen,reg)
        db_ref.child(inmobiliaria).child(usuariosBD).child(id).setValue(crearCuenta)
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