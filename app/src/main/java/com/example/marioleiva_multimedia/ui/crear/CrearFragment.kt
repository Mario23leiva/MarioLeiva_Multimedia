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
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaRecorder
import android.widget.EditText
import android.widget.ImageView
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
    private var mediaRecorder: MediaRecorder? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE_PERMISSIONS = 1001
        private const val CAMERA_REQUEST_CODE = 100
        private const val CAMERA_VIDEO_REQUEST_CODE = 101
        private val AUDIO_CAPTURE_REQUEST_CODE = 102
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

        binding.grabarVideos.setOnClickListener {
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
                val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_VIDEO_REQUEST_CODE)

            }
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
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)

            }
        }

        binding.grabarAudios.setOnClickListener(){
            startAudioCapture()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startAudioCapture() {
        val outputFile = createOutputFile()
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            try {
                prepare()
                start()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error starting audio recording", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createOutputFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        return File.createTempFile(
            "AUDIO_${timeStamp}_",
            ".mp4",
            storageDir
        )
    }

    private fun showDialogForSavingRecordedSoundFile(audioUri: Uri?) {
        // Mostrar un diálogo para que el usuario ingrese el nombre del archivo
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Guardar audio")
        val input = EditText(requireContext())
        input.hint = "Nombre del audio"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            var name = input.text.toString()

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Please enter a name for the audio file", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (!name.endsWith(".mp4")) {
                name += ".mp4"
            }

            val musicDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)

            val outputFile = File(musicDirectory, name)

            try {
                val inputStream = requireContext().contentResolver.openInputStream(audioUri!!)
                val outputStream = FileOutputStream(outputFile)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.flush()
                outputStream.close()

                Toast.makeText(requireContext(), "Audio saved successfully", Toast.LENGTH_SHORT).show()

            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Failed to save audio: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do nothing
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun saveImage(imageBitmap: Bitmap){

        // Mostrar un diálogo para que el usuario ingrese el nombre del archivo
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Guardar foto")
        val input = EditText(requireContext())
        input.hint = "Nombre de la foto"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->

            var name = input.text.toString()
            val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            val file = File(picturesDirectory, if (name.endsWith(".jpg")) name else "$name.jpg")

            Toast.makeText(requireContext(),  file.toString(), Toast.LENGTH_SHORT).show()

            val outputStream = FileOutputStream(file)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()


    }

    private fun dialogVideo(videoUri: Uri?) {
        if (videoUri == null) {
            return
        }
        // Mostrar un diálogo para que el usuario ingrese el nombre del archivo
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Guardar video")
        val input = EditText(requireContext())
        input.hint = "Nombre del video"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            var name = input.text.toString()
            val moviesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            val file = File(moviesDirectory, if (name.endsWith(".mp4")) name else "$name.mp4")

            Toast.makeText(requireContext(), file.toString(), Toast.LENGTH_SHORT).show()

            val inputStream = requireContext().contentResolver.openInputStream(videoUri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.flush()
            outputStream.close()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do nothing
        }

        val dialog = builder.create()
        dialog.show()
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            saveImage(imageBitmap)
        }
        else if (requestCode == CAMERA_VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val videoUri = data?.data
            dialogVideo(videoUri)
        }
        else if (requestCode == AUDIO_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mediaRecorder?.apply {
                stop()
                release()
            }
            showDialogForSavingRecordedSoundFile(data?.data)
        }

    }


}