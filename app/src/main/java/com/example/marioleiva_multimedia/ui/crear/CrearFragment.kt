package com.example.marioleiva_multimedia.ui.crear

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.marioleiva_multimedia.R
import com.example.marioleiva_multimedia.databinding.FragmentCrearBinding
import com.example.marioleiva_multimedia.ui.editar.CrearArchivoTextoFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class CrearFragment : Fragment() {

    private var _binding: FragmentCrearBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val REQUEST_IMAGE_CAPTURE_PERMISSIONS = 1001
        private const val REQUEST_IMAGE_CAPTURE = 1002
        private const val CAMERA_REQUEST_CODE = 1003
        private var currentBitmap: Bitmap? = null

    }


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

        binding.hacerFoto.setOnClickListener {
            // Comprobar si se tiene permiso para acceder a la cámara y guardar archivos externos
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                // Solicitar permisos para acceder a la cámara y guardar archivos externos
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_IMAGE_CAPTURE_PERMISSIONS)

            } else {
                // Si se tienen los permisos necesarios, iniciar la actividad de la cámara
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun guardarFoto(fileName: String) {
        try {
            // Crear el archivo en la carpeta "Pictures" de la aplicación
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val imageFile = File(storageDir, "$fileName.jpg")

            // Obtener la URI del archivo usando el FileProvider
            val photoURI = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", imageFile)

            // Guardar la imagen en el archivo
            val stream: OutputStream = FileOutputStream(imageFile)
            currentBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

            // Agregar la URI del archivo a la galería de imágenes
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = photoURI
            requireContext().sendBroadcast(mediaScanIntent)

            // Mostrar mensaje de éxito
            Toast.makeText(requireContext(), "Foto guardada correctamente", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            // Mostrar mensaje de error
            Toast.makeText(requireContext(), "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            currentBitmap = imageBitmap
            // Mostrar un diálogo para que el usuario ingrese el nombre del archivo
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Guardar foto")
            val input = EditText(requireContext())
            input.hint = "Nombre de la foto"
            builder.setView(input)
            builder.setPositiveButton("Guardar") { _, _ ->
                // Obtener el nombre de la foto ingresado por el usuario
                val fileName = input.text.toString()
                // Guardar la foto con el nombre ingresado por el usuario
                guardarFoto(fileName)
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }
    }


}