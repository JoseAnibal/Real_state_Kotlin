package com.example.reg.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.databinding.FragmentEditarPisoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class EditarPiso : Fragment() {
    val admin by lazy {
        activity as Admin
    }

    val piso by lazy {
        admin.listaPisos.find { it.id==admin.idPiso }
    }

    val listaImagenesUri by lazy {
        mutableListOf<Uri>()
    }
    val PICK_IMAGES_CODE=0

    //FragmentNombrefragmento
    private var _binding: FragmentEditarPisoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentEditarPisoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(4,this::insertarPiso)
    }

    override fun onStart() {
        super.onStart()
        binding.editBaths.setText(piso!!.nbaths)
        binding.editHabs.setText(piso!!.nhabs)
        binding.editCalle.setText(piso!!.calle)
        binding.editDesc.setText(piso!!.descripcion)
        binding.editM2.setText(piso!!.m2!!.toInt().toString())

        //HACER QUE SE PUEDAN GUARDAR CAMBIOS
        binding.editPisoImagen.setOnClickListener {
            pickImagesIntent()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun insertarPiso(v:View){
        if(isValid()){
            GlobalScope.launch(Dispatchers.IO) {
                val calle=binding.editCalle.text.toString().trim()
                val habs=binding.editHabs.text.toString()
                val baths=binding.editBaths.text.toString()
                val m2=binding.editM2.text.toString().toDouble()
                val desc=binding.editDesc.text.toString()
                var listaUrlsFirebase= insertoImagen(piso!!.id.toString(),listaImagenesUri)


                admin.insertoPiso(piso!!.id.toString(),calle, listaUrlsFirebase,habs,baths,m2,desc,false)
                admin.runOnUiThread { admin.navController.navigate(R.id.nav_pisos)}
            }
        }
    }

    suspend fun insertoImagen(id:String,listaFotos: MutableList<Uri>):MutableList<String>{

        lateinit var urlImagenFirebase: MutableList<String>
        urlImagenFirebase= mutableListOf()
        listaFotos!!.forEachIndexed { index, uri ->
            urlImagenFirebase.add(
                sto_ref.child(inmobiliaria).child(pisosBD).child(id).child(generoId(inmobiliaria, pisosBD)!!).putFile(uri).await().storage.downloadUrl.await().toString())
        }

        return urlImagenFirebase
    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.editCalle, this::validoInput),
            Pair(binding.editBaths, this::validoInput),
            Pair(binding.editDesc, this::validoInput),
            Pair(binding.editM2, this::validoInput),
            Pair(binding.editHabs, this::validoInput)
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

    fun validoInput(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty()){
            validado=false
            v.error="Campo vacio"
        }

        return validado
    }

    fun pickImagesIntent(){
        val intent= Intent()
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Selecciona las imagenes"),PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGES_CODE){
            if(resultCode== Activity.RESULT_OK){
                if(data!!.clipData != null){
                    val numImagenes=data.clipData!!.itemCount
                    for(i in 0 until numImagenes){
                        val imageUri=data.clipData!!.getItemAt(i).uri
                        listaImagenesUri.add(imageUri)
                    }
                }else{
                    val imageUri=data.data
                    if (imageUri != null) {
                        listaImagenesUri.add(imageUri)
                    }
                }
            }
        }
    }
}