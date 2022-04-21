import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reg.*
import com.example.reg.databinding.FragmentInicioSesionBinding
import com.example.reg.databinding.FragmentRegistroBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Registro : Fragment() {
    //FragmentNombrefragmento
    private var _binding: FragmentRegistroBinding? = null
    val main by lazy {
        activity as MainActivity
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //FragmentNombrefragmento
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.regBtnRegistrarse.setOnClickListener {
            if(isValid() && coincidenContrasenas(binding.regContrasena,binding.regContrasenaN2)){
                GlobalScope.launch(Dispatchers.IO) {
                    if(!existeUsu(binding.regCorreo.text.toString())){
                        insertUsu()
                        main.runOnUiThread {
                            main.navController.navigate(R.id.navigation_principal)
                        }
                    }else{
                        main.runOnUiThread {
                            binding.regCorreo.error=getString(R.string.correo_registrado)
                        }
                    }
                }
            }else{

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun insertUsu(){
        val correo=binding.regCorreo.text.toString()
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

        if(v.text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(v.text).matches()){
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