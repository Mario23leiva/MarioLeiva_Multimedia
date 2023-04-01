package com.example.marioleiva_multimedia.ui.crear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marioleiva_multimedia.R
import com.example.marioleiva_multimedia.databinding.FragmentCrearBinding
import com.example.marioleiva_multimedia.ui.editar.CrearArchivoTextoFragment

class CrearFragment : Fragment() {

    private var _binding: FragmentCrearBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCrearBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.crearArchivoTexto.setOnClickListener {
            val contenedor = requireActivity().findViewById<View>(R.id.nav_host_fragment_activity_main)
            val fragmentManager = requireFragmentManager()
            val fragmentDestino = CrearArchivoTextoFragment()
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
}