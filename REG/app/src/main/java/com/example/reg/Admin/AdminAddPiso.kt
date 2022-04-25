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
import android.widget.Toast
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.Objetos.Piso
import com.example.reg.databinding.FragmentAdminAddPisoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminAddPiso : Fragment() {
    val admin by lazy {
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminAddPisoBinding? = null
    var listaImagenesUri:ArrayList<Uri>?=null
    val PICK_IMAGES_CODE=0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminAddPisoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        listaImagenesUri= ArrayList()


        binding.addPisoImagen.setOnClickListener {
            pickImagesIntent()
            Toast.makeText(admin.baseContext, "${listaImagenesUri!!.size}", Toast.LENGTH_SHORT).show()
        }



    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(2,this::insertarPiso)
    }

    fun insertarPiso(v:View){
        if(isValid()){
            GlobalScope.launch(Dispatchers.IO) {
                val id= generoId(pisosBD, pisosBD)
                val calle=binding.addCalle.text.toString().trim()
                val habs=binding.addHabs.text.toString()
                val baths=binding.addBaths.text.toString()
                val m2=binding.addM2.text.toString().toDouble()
                val desc=binding.addDescText.text.toString()
                var listaUrlsFirebase= insertoImagen(id!!,listaImagenesUri!!)


                admin.insertoPiso(id.toString(),calle, mutableListOf<String>("uno","dos","tres"),habs,baths,m2,false)
                admin.runOnUiThread { admin.navController.navigate(R.id.nav_pisos)}
            }
        }
    }

    suspend fun insertoImagen(id:String,listaFotos: ArrayList<Uri>):MutableList<String>{

        lateinit var urlImagenFirebase: MutableList<String>
        urlImagenFirebase= mutableListOf()
        listaFotos!!.forEachIndexed { index, uri ->
            urlImagenFirebase.add(sto_ref.child(inmobiliaria).child(pisosBD).child(id).child(generoId(inmobiliaria,
                pisosBD)!!).putFile(uri).await().storage.downloadUrl.await().toString())
        }

        return urlImagenFirebase

    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.addCalle, this::validoInput),
            Pair(binding.addHabs, this::validoInput),
            Pair(binding.addBaths, this::validoInput),
            Pair(binding.addM2, this::validoInput),
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
        val intent=Intent()
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Selecciona las imagenes"),PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGES_CODE){
            if(resultCode==Activity.RESULT_OK){
                if(data!!.clipData != null){
                    val numImagenes=data.clipData!!.itemCount
                    for(i in 0 until numImagenes){
                        val imageUri=data.clipData!!.getItemAt(i).uri
                        listaImagenesUri!!.add(imageUri)
                    }
                }else{

                }
            }
        }
    }
}