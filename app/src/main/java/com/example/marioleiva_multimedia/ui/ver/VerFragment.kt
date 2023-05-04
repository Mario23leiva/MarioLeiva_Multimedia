package com.example.marioleiva_multimedia.ui.ver

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marioleiva_multimedia.R
import com.example.marioleiva_multimedia.databinding.FragmentVerBinding
import com.example.marioleiva_multimedia.VerArchivoTextoFragment
import com.example.marioleiva_multimedia.VerImagenFragment
import java.io.File

class VerFragment : Fragment() {

    private var _binding: FragmentVerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentVerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.verArchivoTexto.setOnClickListener {

            val archivos = obtenerArchivos()
            val bundle = Bundle()
            bundle.putSerializable("archivos", archivos.toTypedArray())

            val contenedor = requireActivity().findViewById<View>(R.id.nav_host_fragment_activity_main)
            val fragmentManager = requireFragmentManager()
            val fragmentDestino = VerArchivoTextoFragment()
            fragmentDestino.arguments = bundle
            fragmentManager.beginTransaction()
                .replace(contenedor.id, fragmentDestino)
                .addToBackStack(null)
                .commit()
        }

        binding.verFoto.setOnClickListener {

            val archivos = obtenerImagenes()
            val bundle = Bundle()
            bundle.putSerializable("imagenes", archivos.toTypedArray())
            bundle.putSerializable("tipo", "imagen")

            val contenedor = requireActivity().findViewById<View>(R.id.nav_host_fragment_activity_main)
            val fragmentManager = requireFragmentManager()
            val fragmentDestino = VerImagenFragment()
            fragmentDestino.arguments = bundle
            fragmentManager.beginTransaction()
                .replace(contenedor.id, fragmentDestino)
                .addToBackStack(null)
                .commit()
        }

        binding.verVideos.setOnClickListener {

            val archivos = obtenerVideos()
            val bundle = Bundle()
            bundle.putSerializable("imagenes", archivos.toTypedArray())
            bundle.putSerializable("tipo", "video")

            val contenedor = requireActivity().findViewById<View>(R.id.nav_host_fragment_activity_main)
            val fragmentManager = requireFragmentManager()
            val fragmentDestino = VerImagenFragment()
            fragmentDestino.arguments = bundle
            fragmentManager.beginTransaction()
                .replace(contenedor.id, fragmentDestino)
                .addToBackStack(null)
                .commit()
        }

        binding.ecucharAudios.setOnClickListener {

            val archivos = obtenerAudios()
            val bundle = Bundle()
            bundle.putSerializable("audio", archivos.toTypedArray())
            bundle.putSerializable("tipo", "audio")

            val contenedor = requireActivity().findViewById<View>(R.id.nav_host_fragment_activity_main)
            val fragmentManager = requireFragmentManager()
            val fragmentDestino = VerImagenFragment()
            fragmentDestino.arguments = bundle
            fragmentManager.beginTransaction()
                .replace(contenedor.id, fragmentDestino)
                .addToBackStack(null)
                .commit()
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


    private fun obtenerImagenes(): MutableList<File> {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return folder.listFiles()?.filter { it.extension == "jpg" }?.toMutableList() ?: mutableListOf()
    }

    private fun obtenerVideos(): MutableList<File> {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        return folder.listFiles()?.filter { it.extension == "mp4" }?.toMutableList() ?: mutableListOf()
    }

    private fun obtenerAudios(): MutableList<File> {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        return folder.listFiles()?.filter { it.extension == "mp3" }?.toMutableList() ?: mutableListOf()
    }
}