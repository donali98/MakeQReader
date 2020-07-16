package com.example.makeqreader.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.makeqreader.R
import com.example.makeqreader.viewmodels.ProductUpdateViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_product.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 * Use the [EditProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProductFragment : Fragment() {
    //Arguments received from the navigation component
    private lateinit var args: EditProductFragmentArgs
    //ViewModel for this fragment
    private lateinit var updateProductViewModel:ProductUpdateViewModel
    //Firebase ref
    private val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args =
            EditProductFragmentArgs.fromBundle(this.requireArguments())
        /* Retrieving  the item info from the arguments and filling the UI fields*/
        updateProductViewModel = ViewModelProvider(this).get(ProductUpdateViewModel::class.java)
        updateProductViewModel.retreiveProductFromApi(args.productId)
        updateProductViewModel.updatedProduct.observe(this, Observer {
            if(it!=null){
                til_edit_name.editText!!.text = Editable.Factory.getInstance().newEditable(it.name)
                til_edit_description.editText!!.text = Editable.Factory.getInstance().newEditable(it.description)
                til_edit_quantity.editText!!.text = Editable.Factory.getInstance().newEditable(it.quantity.toString())
                storage.reference.child("photos/"+it.id+".png").downloadUrl.addOnSuccessListener {uri->
                    Picasso.get().load(uri).fit().centerCrop().into(iv_edit_photo)
                }.addOnFailureListener {err->
                    Log.e("CUSTOM",err.message!!)
                }
            }
        })
        //Observer that will be triggered after an update is succeeded
        updateProductViewModel.productUpdatedId.observe(this, Observer {
            if(it!=null && it.isNotEmpty()){
                try{
                    Toast.makeText(requireContext(),"Product updated successfully", Toast.LENGTH_SHORT).show()

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       //Click listeners for each button
        bt_select_image.setOnClickListener{openGalleryIntent()}
        bt_update_product.setOnClickListener {
            try{
                if(!areInputsNotEmpty()){
                    //Saving product info and uploading image to Firebase
                    if(iv_edit_photo.drawable!= null)
                        uploadImageFromImageViewToFirebase(args.productId)
                    updateProductViewModel.updateProduct(
                        args.productId,
                        til_edit_name.editText!!.text.toString(),
                        til_edit_description.editText!!.text.toString(),
                        Integer.parseInt(til_edit_quantity.editText!!.text.toString())
                    )
                }
            }catch (e: Exception){
                try{
                    Toast.makeText(requireContext(),"Review quantity if its an int", Toast.LENGTH_SHORT).show()

                }catch (r: Exception){
                    r.printStackTrace()
                }
            }
        }
    }
    //Method that will be called after selection of image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) fillImageViewFromIntent(data!!)
    }
    //Function for opening selection image activity
    private fun openGalleryIntent() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    //Function for filling the imageView from the selected picture
    private fun fillImageViewFromIntent(data: Intent) {
        val uri = data.data!!
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        iv_edit_photo.isDrawingCacheEnabled = true
        iv_edit_photo.buildDrawingCache()
        iv_edit_photo.setImageBitmap(bitmap)
    }
    //Checking if inputs are empty
    private fun areInputsNotEmpty(): Boolean =
        til_edit_name.editText!!.text.isEmpty() && til_edit_description.editText!!.text.isEmpty() && til_edit_quantity.editText!!.text.isEmpty()

    //Function for uploading the image contained in imageView to FireBase Cloud Storage
    private fun uploadImageFromImageViewToFirebase(productId:String) {
        val photoRef = storage.reference.child("photos/$productId.png")
        val uploadBitMap = (iv_edit_photo.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        uploadBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val myData = baos.toByteArray()
        val uploadTask = photoRef.putBytes(myData)
        uploadTask.addOnSuccessListener {
            try{
                Toast.makeText(requireContext(), it.metadata.toString(), Toast.LENGTH_SHORT).show()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }.addOnFailureListener {
            Log.e("CUSTOM", it.message!!)
        }
    }
}