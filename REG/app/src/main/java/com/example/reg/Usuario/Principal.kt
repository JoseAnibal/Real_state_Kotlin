import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reg.Actividades.MainActivity
import com.example.reg.databinding.FragmentPrincipalBinding

class Principal : Fragment() {
    val usu by lazy {
        activity as MainActivity
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentPrincipalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentPrincipalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        usu.FAB_manager(1){}
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}