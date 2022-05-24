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
import com.example.reg.Objetos.UsuarioPiso
import com.example.reg.databinding.FilaChatsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class AdaptadorChatsUsuarios(val lista:List<Usuario>, val contexto: Context):
    RecyclerView.Adapter<AdaptadorChatsUsuarios.ViewHolder>(), Filterable {
    val SM by lazy {
        SharedManager(contexto)
    }

    var listaFiltrada = lista
    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .circleCrop()

    class ViewHolder(val bind: FilaChatsBinding): RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]
        with(holder.bind){
            cusuCorreo.text=l.correo
            cusuNombre.text=l.nombre
        }

        holder.bind.clickychat.setOnClickListener {
            (contexto as Admin).idUsu=l.id.toString()
            contexto.navController.navigate(R.id.nav_adminChatPlantilla)
        }

        Glide.with(contexto).load(l.imagen).apply(options).into(holder.bind.cusuImagen)

    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    fun eliminoListaUsuariosSinPiso(usu: Usuario){
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
                        val nombreMinuscula = alu.nombre!!.lowercase(Locale.ROOT)
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