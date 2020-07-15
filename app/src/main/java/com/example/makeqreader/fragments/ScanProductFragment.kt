package com.example.makeqreader.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.makeqreader.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.fragment_scan_product.*

class ScanProductFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_product, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_shot!!.setOnClickListener {
            camera_view!!.captureImage { image ->
                getQRCodeDetails(image.bitmap, requireContext())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        camera_view.start()
    }

    override fun onPause() {
        super.onPause()
        camera_view.stop()
    }

    private fun getQRCodeDetails(bitmap: Bitmap, context: Context) {

        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
            .build()
        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        detector.detectInImage(image)
            .addOnSuccessListener {
                for (firebaseBarcode in it) {
//                    val nextAction = ScanProductFragmentDirections.nextAction(firebaseBarcode.displayValue!!)
//                    Navigation.findNavController(camera_view).navigate(nextAction)
//                    when (firebaseBarcode.valueType) {
//                        //Handle the URL here
//                        FirebaseVisionBarcode.TYPE_URL -> firebaseBarcode.url
//                        // Handle the contact info here, i.e. address, name, phone, etc.
//                        FirebaseVisionBarcode.TYPE_CONTACT_INFO -> firebaseBarcode.contactInfo
//                        // Handle the wifi here, i.e. firebaseBarcode.wifi.ssid, etc.
//                        FirebaseVisionBarcode.TYPE_WIFI -> firebaseBarcode.wifi
//                        //Handle more type of Barcodes
//                    }
                }

            }.addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(context, "Sorry, something went wrong!", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {

//                 Toast.makeText(baseContext, "Completed", Toast.LENGTH_SHORT).show()

            }
    }
}