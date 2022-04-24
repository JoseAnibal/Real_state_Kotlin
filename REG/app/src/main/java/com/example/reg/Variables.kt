package com.example.reg

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

val db_ref= FirebaseDatabase.getInstance().reference
val sto_ref= FirebaseStorage.getInstance().reference

const val inmobiliaria="Inmobiliaria"
const val usuariosBD="Usuarios"
const val pisosBD="Pisos"
const val correoUsu="correo"
const val adminNum=1