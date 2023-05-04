package com.example.marioleiva_multimedia

import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.marioleiva_multimedia.databinding.FragmentEditarImagenBinding
import java.io.File
import java.io.FileOutputStream

class EditarImagen : AppCompatActivity() {

    // Variables para almacenar los datos de la imagen y su edición
    private lateinit var binding: FragmentEditarImagenBinding
    private var originalBitmap: Bitmap? = null
    private var editedBitmap: Bitmap? = null
    private var imagePath: String? = null
    private var nameImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEditarImagenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Cargar la imagen original desde los extras del intent
        imagePath = intent.getStringExtra("imagenRuta")
        nameImage = intent.getStringExtra("imagenNombre")
        originalBitmap = BitmapFactory.decodeFile(imagePath)
        binding.imageView.setImageBitmap(originalBitmap)

        // Asignar las funciones correspondientes a cada botón
        binding.cropButton.setOnClickListener {

        }

        binding.increaseSizeButton.setOnClickListener {
            editedBitmap = increaseImageSize(originalBitmap!!, 1.5f)
            binding.imageView.setImageBitmap(editedBitmap)
            originalBitmap = editedBitmap
        }

        binding.decreaseSizeButton.setOnClickListener {
            editedBitmap = decreaseImageSize(originalBitmap!!, 2.0f)
            binding.imageView.setImageBitmap(editedBitmap)
            originalBitmap = editedBitmap
        }

        binding.filterButton.setOnClickListener {
            editedBitmap = applyGrayscaleFilter(originalBitmap!!)
            originalBitmap = editedBitmap
        }

        binding.saveButton.setOnClickListener {
            saveImage()
        }
    }

    // Aplicar un filtro de escala de grises a la imagen
    private fun applyGrayscaleFilter(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val output = Bitmap.createBitmap(width, height, bitmap.config)

        val canvas = Canvas(output)
        val paint = Paint()
        val matrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        val filter = ColorMatrixColorFilter(matrix)
        paint.colorFilter = filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        editedBitmap = output
        binding.imageView.setImageBitmap(editedBitmap)

        return output
    }

    // Aumentar el tamaño de la imagen según un factor de escala
    private fun increaseImageSize(bitmap: Bitmap, scaleFactor: Float): Bitmap {
        val width = (bitmap.width * scaleFactor).toInt()
        val height = (bitmap.height * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    // Disminuir el tamaño de la imagen según un factor de escala
    private fun decreaseImageSize(bitmap: Bitmap, scaleFactor: Float): Bitmap {
        val width = (bitmap.width / scaleFactor).toInt()
        val height = (bitmap.height / scaleFactor).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    // Recortar la imagen según los parámetros de entrada
    private fun cropImage(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(bitmap, x, y, width, height)

    }

    // Guardar la imagen editada en la galería del dispositivo
    private fun saveImage() {
        val picturesFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val filename = nameImage
        val file = File(picturesFolder, filename)
        try {
            val outputStream = FileOutputStream(file)
            editedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            showConfirmationDialog(file) // Mostrar diálogo de confirmación
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: Mostrar un mensaje de error al usuario
        }
    }

    // Mostrar un diálogo de confirmación para guardar la imagen editada
    private fun showConfirmationDialog(file: File) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Do you want to save the edited image?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Guardar la imagen
            // TODO: Mostrar un mensaje de éxito al usuario
            // Enviar la imagen a la galería de fotos del usuario
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(file)
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
            finish()
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->
            finish()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val CROP_IMAGE_REQUEST_CODE = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Si se recibió el código de solicitud correcto y el resultado es satisfactorio,
        // actualizar la imagen editada con la imagen recortada
        if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val croppedBitmap = data?.extras?.getParcelable<Bitmap>("data")
            editedBitmap = croppedBitmap
            binding.imageView.setImageBitmap(editedBitmap)
        }
    }
}
