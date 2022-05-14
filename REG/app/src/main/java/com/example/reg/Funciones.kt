package com.example.reg

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.reg.Objetos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch


fun insertoUsuarioBD(id:String,correo:String,nombre:String,password:String,tipo:Int,imagen:String){
    val crearCuenta=Usuario(id,correo,nombre, password, tipo,imagen)
    db_ref.child(inmobiliaria).child(usuariosBD).child(id).setValue(crearCuenta)
}

fun generoId(rama:String,objeto:String): String? {
    return db_ref.child(rama).child(objeto).push().key
}

fun redireccionar(c:Context, ac:Activity){
    Intent(c,ac::class.java).also {
        c.startActivity(it)
    }
}

fun existeUsu(correo:String):Boolean{
    var existe=false
    val semaforo= CountDownLatch(1)

    db_ref.child(inmobiliaria).child(usuariosBD).orderByChild(correoUsu).equalTo(correo)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChildren()){
                    existe=true
                }
                semaforo.countDown()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    semaforo.await()
    return existe
}

fun sacoUsuarioDeLaBase(correoUsuario:String):Usuario{
    var usuario=Usuario()
    val semaforo= CountDownLatch(1)

    db_ref.child(inmobiliaria).child(usuariosBD).orderByChild(correoUsu).equalTo(correoUsuario)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChildren()){
                   usuario= snapshot.children.iterator().next().getValue(Usuario::class.java)!!
                }
                semaforo.countDown()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    semaforo.await()
    return usuario
}
