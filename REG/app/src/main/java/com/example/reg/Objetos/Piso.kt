package com.example.reg.Objetos

class Piso(val id:String?=null,
           val calle:String?=null,
           val imagenes:MutableList<String>?= mutableListOf(),
           val nHabs:Int?=0,
           val nBaths:Int?=0,
           val m2:Double?=0.0,
           val estado:Boolean?=false)