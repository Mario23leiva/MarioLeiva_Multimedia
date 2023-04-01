package com.example.marioleiva_multimedia.ui.editar

import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marioleiva_multimedia.databinding.CrearArchivoTextoFragmentBinding
import java.io.File
import java.io.FileOutputStream

class CrearArchivoTextoFragment : Fragment() {

    private var _binding: CrearArchivoTextoFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CrearArchivoTextoFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentSizeSp = binding.contenidoArchivo.textSize / resources.displayMetrics.scaledDensity

        binding.masGrande.setOnClickListener{
            binding.contenidoArchivo.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentSizeSp + 1)
            currentSizeSp += 1
            binding.textViewTextSize.text = currentSizeSp.toString() + "sp"
        }

        binding.masPeque.setOnClickListener{
            binding.contenidoArchivo.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentSizeSp - 1)
            currentSizeSp -= 1
            binding.textViewTextSize.text = currentSizeSp.toString() + "sp"
        }

        // Establecer el tipo de fuente inicial
        binding.contenidoArchivo.setTypeface(null, Typeface.NORMAL)

        binding.btnTextoNegrita.setOnClickListener {
            if (binding.contenidoArchivo.typeface?.style == Typeface.ITALIC || binding.contenidoArchivo.typeface?.style == Typeface.BOLD_ITALIC) {
                binding.contenidoArchivo.setTypeface(null, Typeface.BOLD_ITALIC)
            } else {
                binding.contenidoArchivo.setTypeface(null, Typeface.BOLD)
            }
        }

        binding.btnTextoCursiva.setOnClickListener {
            if (binding.contenidoArchivo.typeface?.style == Typeface.BOLD || binding.contenidoArchivo.typeface?.style == Typeface.BOLD_ITALIC) {
                binding.contenidoArchivo.setTypeface(null, Typeface.BOLD_ITALIC)
            } else {
                binding.contenidoArchivo.setTypeface(null, Typeface.ITALIC)
            }
        }

        binding.btnTextoNormal.setOnClickListener {
            binding.contenidoArchivo.setTypeface(null, Typeface.NORMAL)
        }

        binding.btnGuardarArchivoTexto.setOnClickListener{
            guardarArchivo(binding.nombreArchivo.text.toString() + ".txt", binding.contenidoArchivo.text.toString())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun guardarArchivo(nombreArchivo: String, contenidoArchivo: String) {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(folder, nombreArchivo)

        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(contenidoArchivo.toByteArray())
            }
            Toast.makeText(this.context, "Archivo guardado correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this.context, "Error al guardar el archivo", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

}