import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reg.MainActivity
import com.example.reg.Objetos.Usuario
import com.example.reg.R
import com.example.reg.SharedManager
import com.example.reg.databinding.FragmentInicioSesionBinding
import com.example.reg.sacoUsuarioDeLaBase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InicioSesion : Fragment() {
    //FragmentNombrefragmento
    val main by lazy {
        activity as MainActivity
    }
    val SM by lazy{
        SharedManager(main.baseContext)
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
                    val usuario=sacoUsuarioDeLaBase(binding.loginCorreo.text.toString())
                    if(comprobacion(usuario!!)){
                        main.runOnUiThread{
                            SM.idUsuario="admin"
                        }
                    }else{
                        main.runOnUiThread{
                            SM.idUsuario=usuario.id.toString()
                        }
                    }
                }
            }
        }

        binding.loginTvRegistrarse.setOnClickListener {
            main.navController.navigate(R.id.navigation_registro)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun comprobacion(objeto:Usuario):Boolean{
        var pasa=false
        if(objeto.correo==binding.loginCorreo.text.toString() && objeto.password==binding.loginContrasena.text.toString()){
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