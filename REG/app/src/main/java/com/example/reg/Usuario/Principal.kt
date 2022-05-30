import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.MainActivity
import com.example.reg.AdaptadoresRecycler.AdaptadorFotosPiso
import com.example.reg.R
import com.example.reg.databinding.FragmentPrincipalBinding


@Suppress("DEPRECATION")
class Principal : Fragment() {
    val usu by lazy {
        activity as MainActivity
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentPrincipalBinding? = null
    val adaptadorListaImagenesPisos by lazy {
        AdaptadorFotosPiso(usu.piso.imagenes!!.toMutableList(),usu.contexto)
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentPrincipalBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.modoOscuro)
        menu.removeItem(R.id.estadisticaFactura)
    }

    override fun onResume() {
        super.onResume()
        usu.FAB_manager(1){}
    }


    override fun onStart() {
        super.onStart()
        binding.pisHabs.text=usu.piso.nhabs
        binding.pisBath.text=usu.piso.nbaths
        binding.pisM2.text=usu.piso.m2

        binding.rvFotitos.adapter=adaptadorListaImagenesPisos
        binding.rvFotitos.layoutManager= LinearLayoutManager(usu.contexto,
            LinearLayoutManager.HORIZONTAL, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}