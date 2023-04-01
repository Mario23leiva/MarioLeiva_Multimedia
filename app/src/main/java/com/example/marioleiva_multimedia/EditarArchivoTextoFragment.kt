package com.example.marioleiva_multimedia.ui.ver

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marioleiva_multimedia.R
import com.example.marioleiva_multimedia.databinding.EditarArchivoTextoFragmentBinding
import com.example.marioleiva_multimedia.ui.editar.CrearArchivoTextoFragment
import com.example.marioleiva_multimedia.ui.editar.VerArchivoTextoFragment
import java.io.*

class EditarArchivoTextoFragment : Fragment() {

    private var _binding: EditarArchivoTextoFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = EditarArchivoTextoFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val archivos = arguments?.getSerializable("archivos") as? Array<File> ?: emptyArray()
        var archivo: File? = null

        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, archivos)
        binding.listaArchivos.adapter = adaptador

        binding.listaArchivos.setOnItemClickListener { _, _, position, _ ->
            archivo = archivos[position]
            try {
                val fileReader = FileReader(archivo)
                val bufferedReader = BufferedReader(fileReader)
                var linea: String? = ""
                var contenido = ""

                while (bufferedReader.readLine().also { linea = it } != null) {
                    contenido += linea + "\n"
                }
                bufferedReader.close()

                binding.editarTextoArchivo.setText(contenido)
            } catch (e: IOException) {
                Toast.makeText(this.context, "Error al leer el archivo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGuardarArchivoTextoEditado.setOnClickListener {
            val nuevoContenido = binding.editarTextoArchivo.text.toString()

            try {
                val fileWriter = FileWriter(archivo)
                val bufferedWriter = BufferedWriter(fileWriter)
                bufferedWriter.write(nuevoContenido)
                bufferedWriter.close()

                Toast.makeText(this.context, "Cambios guardados", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this.context, "Error al guardar cambios", Toast.LENGTH_SHORT).show()
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}