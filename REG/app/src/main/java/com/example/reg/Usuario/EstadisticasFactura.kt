package com.example.reg.Usuario

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.MainActivity
import com.example.reg.R
import com.example.reg.databinding.FragmentEstadisticasFacturaBinding
import com.example.reg.databinding.FragmentFacturasBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class EstadisticasFactura : Fragment() {
    val usu by lazy {
        activity as MainActivity
    }
    lateinit var pieData: PieData
    //FragmentNombrefragmento
    private var _binding: FragmentEstadisticasFacturaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //FragmentNombrefragmento
        _binding = FragmentEstadisticasFacturaBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val stats=usu.generoEstadistica()
        val pieEntries = mutableListOf<PieEntry>()
        stats.map{
            pieEntries.add(PieEntry(it.porcentaje.toFloat(), it.nombre))
        }
        val label = "| Colores"
        val pieDataSet = PieDataSet(pieEntries, label)
        pieDataSet.valueTextSize = 12f
        pieDataSet.colors = stats.map{it.color}
        pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        val pieChart = binding.Pie
        pieChart.data = pieData
        pieChart.legend.isEnabled = true
        pieChart.description.text = "Porcentaje Facturas"
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.modoOscuro)
    }

    override fun onResume() {
        super.onResume()
        usu.FAB_manager(0){}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}