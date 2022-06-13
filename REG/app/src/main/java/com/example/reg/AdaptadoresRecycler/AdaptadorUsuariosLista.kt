package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.reg.Actividades.LoggedUser
import com.example.reg.Actividades.NotLoggedUserVerPiso
import com.example.reg.Objetos.Piso
import com.example.reg.Objetos.Usuario
import com.example.reg.Objetos.UsuarioPiso
import com.example.reg.databinding.FilaPisoBinding
import com.example.reg.databinding.FilaUsuariosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class AdaptadorUsuariosLista(val lista:List<Usuario>, val contexto:Context,val eliminar:Int):RecyclerView.Adapter<AdaptadorUsuariosLista.ViewHolder>(), Filterable {
    val SM by lazy {
        SharedManager(contexto)
    }
    var listaFiltrada = lista
    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .circleCrop()

    class ViewHolder(val bind: FilaUsuariosBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaUsuariosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]

        holder.bind.usuAceptar.apply {
            visibility=View.INVISIBLE
            isEnabled=false
        }

        holder.bind.usuRechazar.apply {
            visibility=View.INVISIBLE
            isEnabled=false
        }

        with(holder.bind){
            usuCorreo.text=l.correo
            usuNombre.text=l.nombre
        }

        holder.bind.clicky.setOnClickListener {
            (contexto as Admin).idUsu=l.id.toString()
            contexto.navController.navigate(R.id.nav_editarUsuario)
        }

        Glide.with(contexto).load(l.imagen).apply(options).into(holder.bind.usuImagen)
        if(l.resgistrado == true){
            Glide.with(contexto).load(listReg[0]).into(holder.bind.usuRegistrado)
        }else{
            Glide.with(contexto).load(listReg[1]).into(holder.bind.usuRegistrado)
        }

        if(eliminar==1){
            holder.bind.usuRechazar.apply {
                visibility=View.VISIBLE
                isEnabled=true
                setOnClickListener {
                    eliminoListaUsuariosSinPiso(l)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    fun eliminoListaUsuariosSinPiso(usu:Usuario){
        val lista= mutableListOf<String>()
        val listadevolver= mutableListOf<Usuario>()
        db_ref.child(inmobiliaria)
            .child(UsuarioPisoBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    listadevolver.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(UsuarioPiso::class.java)
                        if (ussu != null && ussu.idUsuario==usu.id) {
                            db_ref.child(inmobiliaria).child(UsuarioPisoBD).child(ussu.id!!).removeValue()
                            db_ref.child(inmobiliaria).child(usuariosBD).child(ussu.idUsuario!!).child("resgistrado").setValue(false)
                        }
                        db_ref.child(inmobiliaria)
                            .child(UsuarioPisoBD).removeEventListener(this)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
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