
package com.pharmacure.online_pharmacy_app.common

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.pharmacure.online_pharmacy_app.BuildConfig
import com.pharmacure.online_pharmacy_app.activities.DrugDetailsActivity
import com.pharmacure.online_pharmacy_app.activities.auth.LoginActivity
import com.pharmacure.online_pharmacy_app.activities.auth.SignUpActivity
import com.pharmacure.online_pharmacy_app.activities.base.BaseBottomNavigationActivity
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*


const val FULL_NAME = "NAME"
const val EMAIL = "EMAIL"
const val PHOTO = "PHOTO"
const val TOKEN = "TOKEN"
const val DATE = "DATE"

//
//const val SERVER_URL =  BuildConfig.LOCAL_SERVER
//const val SERVER_URL = "http://10.0.2.2:3000/"
const val SERVER_URL =  BuildConfig.SERVER_URL

//Extra Constants
const val CATEGORY = "CATEGORY"
const val DRUG_ID = "DRUG_ID"
const val CUSTOMER_ID = "CUSTOMER_ID"
const val FRAG_TO_OPEN = "FRAG_TO_OPEN"

//Generate uuid for orders
fun uniqueId() = UUID.randomUUID().toString()

//Fragments
const val OPEN_CART_FRAGS = "CART_FRAGS"

const val OPEN_ORDERS_FRAG = "OPEN_ORDERS_FRAG"


fun Context.startHomeActivity(fragment: String) =
    Intent(this, BaseBottomNavigationActivity::class.java).also {
        it.putExtra(FRAG_TO_OPEN, fragment)
        startActivity(it)
    }



fun showProgressDialog(progressDialog: ProgressDialog?, message: String) {
    progressDialog!!.apply {
        setMessage(message)
        setTitle("Please wait")
        show()
    }
}

fun hideProgressDialog(progressDialog: ProgressDialog?) {
    progressDialog?.hide()
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : DIAware, T : Lifecycle {
    return lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this as AppCompatActivity, direct.instance<ViewModelFactory>())[VM::class.java]
    }
}

fun Context.startDrugDetailsPage(drugID: Int, customerID: Int) =
    Intent(this, DrugDetailsActivity::class.java).also {
        it.putExtra(DRUG_ID, drugID)
        it.putExtra(CUSTOMER_ID, customerID)
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        startActivity(it)
    }


fun Context.startSignUpActivity()=
    Intent(this,SignUpActivity::class.java).also {
        startActivity(it)
    }




fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
    //create a file to write bitmap data
    var file: File? = null
     try {
        file = File("""${Environment.getExternalStorageDirectory()}${File.separator}$fileNameToSave""")
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
       return file
    } catch (e: Exception) {
        e.printStackTrace()
      return  file // it will return null
    }
}

fun convertBitMapToInputStream(bitmap: Bitmap): ByteArrayInputStream {

    val byteSize = bitmap.rowBytes * bitmap.height
    val byteBuffer: ByteBuffer = ByteBuffer.allocate(byteSize)
    bitmap.copyPixelsToBuffer(byteBuffer)

// Get the byteArray.
    val byteArray: ByteArray = byteBuffer.array()

// Get the ByteArrayInputStream.

    return ByteArrayInputStream(byteArray)
}

