package com.example.marioleiva_multimedia.ui.editar

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marioleiva_multimedia.R
import com.example.marioleiva_multimedia.databinding.FragmentEditarBinding
import com.example.marioleiva_multimedia.ui.ver.EditarArchivoTextoFragment
import java.io.File

class EditarFragment : Fragment() {

    private var _binding: FragmentEditarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val editarViewModel =
            ViewModelProvider(this).get(EditarViewModel::class.java)

        _binding = FragmentEditarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.editarArchivoTexto.setOnClickListener {

            val archivos = obtenerArchivos()
            val bundle = Bundle()
            bundle.putSerializable("archivos", archivos.toTypedArray())

            val contenedor = requireActivity().findViewById<View>(R.id.nav_host_fragment_activity_main)
            val fragmentManager = requireFragmentManager()
            val fragmentDestino = EditarArchivoTextoFragment()
            fragmentDestino.arguments = bundle
            fragmentManager.beginTransaction()
                .replace(contenedor.id, fragmentDestino)
                .addToBackStack(null)
                .commit()
        }

        binding.editarFoto.setOnClickListener(){

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtenerArchivos(): MutableList<File> {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return folder.listFiles()?.filter { it.extension == "txt" }?.toMutableList() ?: mutableListOf()
    }
}