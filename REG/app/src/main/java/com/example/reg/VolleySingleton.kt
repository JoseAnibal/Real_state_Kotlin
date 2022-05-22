package com.example.reg

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton(context: Context) {
    companion object {
        //Al marcarlo como volatil el objeto será creado en RAM y no en caché.
        //Esto hace que todas las posibles hebras que accedan al objeto siempre accdan a los mismos
        // datos
        //Es decir perdemos eficiencia pero ganamos el quitarnos problemas de sincronización
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
        // ¿He creado ya el objeto?, si no, lo creo, si ya esta creado no hago nada
            // Además comprobamos que no puedan acceder dos hebras a la vez
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    //En programción hay dos formas de inicializar una variable: eager (por defecto) y lazy
    //La variable requestQueue no será inicialiazada hasta que alguien hago uso de ella
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    //Función genérica, nos permite llamar a addToRequestQueue con distintos tipos de Request
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}