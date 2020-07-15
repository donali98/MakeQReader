package com.example.makeqreader.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.makeqreader.R
import com.example.makeqreader.viewmodels.ProductSaveViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private val storage = Firebase.storage
    private lateinit var productViewModel: ProductSaveViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productViewModel = ViewModelProvider(this).get(ProductSaveViewModel::class.java)
        productViewModel.productInsertedId.observe(this, Observer {
            if (it != null && it != "") {
//                Log.d("CUSTOM", "Changed $it")
                val width = resources.configuration.screenWidthDp
                val height = resources.configuration.screenHeightDp
                val smallerDimension = if (width < height) width else height
                val qrgEncoder =
                    QRGEncoder(it, null, QRGContents.Type.TEXT, smallerDimension)
                qrgEncoder.colorBlack = Color.WHITE
                qrgEncoder.colorWhite = Color.BLACK
                try {
                    // Getting QR-Code as Bitmap
                    val bitmap = qrgEncoder.bitmap
                    // Setting Bitmap to ImageView
                    iv_qr.setImageBitmap(bitmap)
                    uploadImageFromImageViewToFirebase(iv_qr,it, "${it}.png")
                    if (iv_photo.drawable != null) {
                        uploadImageFromImageViewToFirebase(iv_photo,it, "photos/${it}.png")

                    }
                } catch (e: Exception) {
                    Log.e("CUSTOM", e.toString())
                }
            }

        })
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bt_select_image.setOnClickListener { openGalleryIntent() }
        bt_save_product.setOnClickListener {

            if (!areInputsNotEmpty()) {
                productViewModel.saveProduct(
                    til_name.editText!!.text.toString(),
                    til_description.editText!!.text.toString(),
                    Integer.parseInt(til_quantity.editText!!.text.toString())
                )
            } else {
                try {
                    Toast.makeText(context, "Please fill all the imputs", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) fillImageViewFromIntent(data!!)
    }
    private fun openGalleryIntent() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    private fun areInputsNotEmpty(): Boolean =
        til_name.editText!!.text.isEmpty() && til_description.editText!!.text.isEmpty() && til_quantity.editText!!.text.isEmpty()

    //Function for uploading the image contained in imageView to FireBase Cloud Storage
    private fun uploadImageFromImageViewToFirebase(imageView: ImageView, productId: String, pathString: String) {
        val photoRef = storage.reference.child(pathString)
        val uploadBitMap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        uploadBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val myData = baos.toByteArray()
        val uploadTask = photoRef.putBytes(myData)
        uploadTask.addOnSuccessListener {
            try {
                Toast.makeText(context, it.metadata.toString(), Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.addOnFailureListener {
            Log.e("CUSTOM", it.message!!)
        }
    }
    //Function for filling the imageView from the selected picture
    private fun fillImageViewFromIntent(data: Intent) {
        val uri = data.data!!
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        iv_photo.isDrawingCacheEnabled = true
        iv_photo.buildDrawingCache()
        iv_photo.setImageBitmap(bitmap)
    }


}