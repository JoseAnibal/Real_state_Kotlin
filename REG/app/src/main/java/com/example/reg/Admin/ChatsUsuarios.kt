package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.Admin
import com.example.reg.R
import com.example.reg.databinding.FragmentChatsUsuariosBinding

class ChatsUsuarios : Fragment() {
    val admin by lazy {
        activity as Admin
    }
    //FragmentNombrefragmento
    private var _binding: FragmentChatsUsuariosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //FragmentNombrefragmento
        _binding = FragmentChatsUsuariosBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.eliminarTodaRelacion)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(5){}
    }

    override fun onStart() {
        super.onStart()
        binding.rvChatsUsuarios.adapter=admin.adaptadorSalasUsuarios
        binding.rvChatsUsuarios.layoutManager= LinearLayoutManager(admin.contexto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}