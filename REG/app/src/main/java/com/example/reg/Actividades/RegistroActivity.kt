package com.example.reg.Actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.reg.*
import com.example.reg.databinding.ActivityRegistroBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.regBtnRegistrarse.setOnClickListener {
            if(isValid() && coincidenContrasenas(binding.regContrasena,binding.regContrasenaN2)){
                GlobalScope.launch(Dispatchers.IO) {
                    if(!existeUsu(binding.regCorreo.text.toString())){
                        insertUsu()
                        runOnUiThread {
                            redireccionar(baseContext,LoggedUser())
                        }
                    }else{
                        runOnUiThread {
                            binding.regCorreo.error=getString(R.string.correo_registrado)
                        }
                    }
                }
            }
        }
    }

    fun insertUsu(){
        val correo=binding.regCorreo.text.toString().trim()
        val nombre=binding.regNombre.text.toString()
        val password=binding.regContrasena.text.toString()
        val genero_id= generoId(inmobiliaria, usuariosBD).toString()

        insertoUsuarioBD(genero_id,correo,nombre,password,0)
    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.regNombre, this::validoCampo),
            Pair(binding.regCorreo, this::validoCorreo),
            Pair(binding.regContrasena, this::validoCampo)

        )
        for(c in checkers){
            val x = c.first
            val f = c.second
            val y = f(x)
            validado = y
            if(!validado) break
        }
        return validado
    }

    fun coincidenContrasenas(v: EditText, b: EditText):Boolean{
        var validado=true

        if(b.text.toString()!=v.text.toString()){
            validado=false
            b.error=getString(R.string.inc_contraseñas)
        }

        return validado
    }

    fun validoCorreo(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(v.text.trim()).matches()){
            validado=false
            v.error=getString(R.string.valid_correo)
        }

        return validado
    }

    fun validoCampo(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty()){
            validado=false
            v.error=getString(R.string.campo_vacio)
        }

        return validado
    }
}