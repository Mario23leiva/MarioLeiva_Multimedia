package com.example.marioleiva_multimedia

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marioleiva_multimedia.databinding.FragmentVerBinding
import com.example.marioleiva_multimedia.databinding.FragmentVerImagenBinding
import java.io.File
import java.io.FileOutputStream

class VerImagenFragment : Fragment() {

    private var _binding: FragmentVerImagenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var imagenRuta:String = "null"
    private var imagenNombre:String = "null"
    private var tipoArchivo:String = "null"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVerImagenBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val items = arguments?.getSerializable("imagenes") as? Array<File> ?: emptyArray()
        tipoArchivo = arguments?.getString("tipo").toString()

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, items.map { it.path })
        binding.listaImagenes.adapter = adapter

        binding.listaImagenes.setOnItemClickListener { _, _, position, _ ->
            val file = items[position]
            imagenRuta = file.path
            imagenNombre = file.name
            if(tipoArchivo.equals("imagen")){
                binding.mostrarImagenArchivo.setImageBitmap(BitmapFactory.decodeFile(imagenRuta))
            }
            else if(tipoArchivo.equals("video")){
                val intent = Intent(requireContext(), VerVideos::class.java)
                intent.putExtra("nombreRuta", imagenRuta)
                startActivity(intent)
            }
        }

        binding.btnEditarImagen.setOnClickListener(){
            val intent = Intent(requireContext(), EditarImagen::class.java)
            intent.putExtra("imagenRuta", imagenRuta)
            intent.putExtra("imagenNombre", imagenNombre)
            startActivity(intent)
        }





        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}