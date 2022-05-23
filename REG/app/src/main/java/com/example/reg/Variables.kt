package com.example.reg

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

val db_ref= FirebaseDatabase.getInstance().reference
val sto_ref= FirebaseStorage.getInstance().reference

val listReg= listOf(R.drawable.registrado,R.drawable.nregistrado)

const val inmobiliaria="Inmobiliaria"
const val usuariosBD="Usuarios"
const val pisosBD="Pisos"
const val correoUsu="correo"
const val UsuarioPisoBD="UsuarioPiso"
const val facturaBD="Facturas"
const val chatBD="Chats"
const val mensajeBD="Mensajes"
const val adminNum=1