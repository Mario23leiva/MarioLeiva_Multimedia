package com.example.marioleiva_multimedia.ui.editar

import android.R
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marioleiva_multimedia.databinding.VerArchivoTextoFragmentBinding
import java.io.File
import java.io.FileOutputStream

class VerArchivoTextoFragment : Fragment() {

    private var _binding: VerArchivoTextoFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VerArchivoTextoFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val archivos = arguments?.getSerializable("archivos") as? Array<File> ?: emptyArray()


        val adaptador = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, archivos)
        binding.listaArchivos.adapter = adaptador

        binding.listaArchivos.setOnItemClickListener { _, _, position, _ ->
            val archivo = archivos[position]
            try {
                val contenido = archivo.readText()
                binding.mostrarTextoArchivo.text = contenido
            } catch (e: Exception) {
                binding.mostrarTextoArchivo.text = "Error al leer el archivo"
            }
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