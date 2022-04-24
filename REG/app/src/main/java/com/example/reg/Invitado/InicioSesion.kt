package com.example.reg.Invitado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.Actividades.LoggedUser
import com.example.reg.Actividades.MainActivity
import com.example.reg.Actividades.RegistroActivity
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.FragmentInicioSesionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InicioSesion : Fragment() {
    //FragmentNombrefragmento
    val tabbed by lazy {
        activity as LoggedUser
    }
    val SM by lazy{
        SharedManager(tabbed.baseContext)
    }
    private var _binding: FragmentInicioSesionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //FragmentNombrefragmento
        _binding = FragmentInicioSesionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.loginBtnLogin.setOnClickListener {
            if(validoInput(binding.loginCorreo) && validoInput(binding.loginContrasena)){
                GlobalScope.launch(Dispatchers.IO) {
                    val usuario=sacoUsuarioDeLaBase(binding.loginCorreo.text.toString().trim())
                    if(comprobacion(usuario)){
                        if(usuario.tipo==adminNum){
                            tabbed.runOnUiThread{
                                SM.idUsuario="admin"
                                redireccionar(tabbed.baseContext, Admin())
                            }
                        }else{
                            tabbed.runOnUiThread{
                                SM.idUsuario=usuario.id.toString()
                                redireccionar(tabbed.baseContext, MainActivity())
                            }
                        }
                    }else{
                        tabbed.runOnUiThread {
                            binding.loginError.text="Datos incorrectos"
                        }
                    }
                }
            }
        }

        binding.loginTvRegistrarse.setOnClickListener {
            redireccionar(tabbed.baseContext, RegistroActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun comprobacion(objeto:Usuario):Boolean{
        var pasa=false
        if(objeto.correo==binding.loginCorreo.text.toString().trim() && objeto.password==binding.loginContrasena.text.toString()){
            pasa=true
        }
        return pasa
    }

    fun validoInput(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty()){
            validado=false
            v.error=getString(R.string.campo_vacio)
        }

        return validado
    }
}