package com.pharmacure.online_pharmacy_app.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.ApiResponse
import com.github.florent37.inlineactivityresult.kotlin.InlineActivityResultException
import com.github.florent37.inlineactivityresult.kotlin.coroutines.startForResult
import com.rasalexman.coroutinesmanager.CoroutinesProvider.UI
import kotlinx.android.synthetic.main.activity_order_by_prescription.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance
import java.io.*
import kotlin.coroutines.CoroutineContext



class OrderByPrescriptionActivity : AppCompatActivity(), CoroutineScope, DIAware {
    private  var progressDialog: ProgressDialog?=null
    private   val REQUEST_CODE  =1

    override val di: DI by di(this)
    private val factory: ViewModelFactory by instance()
    private lateinit var customerViewModel: CustomerViewModel

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var bitmap: MutableLiveData<Bitmap> = MutableLiveData()
     private var customer_id:Int?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

           customer_id = intent.getIntExtra(CUSTOMER_ID,0)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                log { "Permission granted" }
            }else{
                requestPermission()
            }
        } else {
            log { "SDK less than android version 6" }
        }
        setContentView(R.layout.activity_order_by_prescription)

        progressDialog = ProgressDialog(this)


        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)

        backButtonPrescriptionImageView.setOnClickListener {
            finish()
        }

        captureImageButton.setOnClickListener {
            captureImage()
        }

        orderByPrescriptionButton.setOnClickListener {
            bitmap.let { bitmap ->
                if (bitmap.value != null) {
                    val part = "presc_image_url".buildImageBodyPart(bitmap.value!!)
                    customer_id.let {customerID ->
                      if(it !=null){
                          customerViewModel.orderByPrescription(customerID!!,part, uniqueId()).observe(this,Observer(::handleOrderResult))
                      }
                  }
                } else {
                    Toast.makeText(this, "Please take image of you prescription", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }


    }

    private suspend fun uploadImageToServer(customerID:Int,part: MultipartBody.Part,orderRef:String) =
        customerViewModel.orderByPrescription(customerID,part,orderRef)


    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    private fun captureImage(){
        launch(UI) {
            try {
                val result = startForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                log { "Image content ...." }
                log { "$imageBitmap" }

                bitmap.value = imageBitmap

                prescriptionImageView.setImageBitmap(imageBitmap)
            } catch (e: InlineActivityResultException) {
                log { "Error occurred ${e.data}" }
            }
        }

    }

    private fun String.buildImageBodyPart(bitmap: Bitmap):  MultipartBody.Part {
        val MEDIA_TYPE_IMAGE: MediaType? = "image/*".toMediaTypeOrNull()
        val leftImageFile = convertBitmapToFile("$this.png", bitmap)
        val reqFile = leftImageFile.asRequestBody(MEDIA_TYPE_IMAGE)
        return MultipartBody.Part.createFormData(this, leftImageFile.name, reqFile)
    }


   private fun  handleOrderResult(result:SResult<ApiResponse>){
       when (result) {
           is SResult.Loading -> {
          showProgressDialog(progressDialog,"Placing order please wait...")
           }
           is SResult.Success -> {
           hideProgressDialog(progressDialog)

               Handler(Looper.getMainLooper()).post {
                   Toast.makeText(this,"Prescription has been received ",Toast.LENGTH_LONG).show()
               }

               this.startHomeActivity(FRAG_TO_OPEN)
           }
           is SResult.Error -> {
               hideProgressDialog(progressDialog)
               log { " Error => ${result.message}" }
               Handler(Looper.getMainLooper()).post {
                   Toast.makeText(this,"No Response from server ",Toast.LENGTH_LONG).show()

               }

           }
           is SResult.Empty -> {
               hideProgressDialog(progressDialog)
               Handler(Looper.getMainLooper()).post {
                   Toast.makeText(this,"No Response from server ",Toast.LENGTH_LONG).show()
               }

           }
       }
   }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this.cacheDir, fileName)

        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


//        private fun pickImage() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_PHOTO)
//    }


//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
//            try {
//                data?.let {
//                    val inputStream: InputStream? =
//                        this.contentResolver?.openInputStream(it.data!!)
//                    inputStream?.let { stream ->
//                        launch(UI){
//                            customerViewModel.orderByPrescription(stream)
//                        }
//                    }
//                }
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//                log { "Image not taken due to exception :${e}" }
//            }
//        }
//    }
//
//    companion object {
//        const val PICK_PHOTO = 1
//    }

}