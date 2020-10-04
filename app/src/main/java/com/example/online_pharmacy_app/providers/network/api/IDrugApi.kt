package com.example.online_pharmacy_app.providers.network.api

import com.example.online_pharmacy_app.viewobjects.Drug
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IDrugApi {

    @GET("drug/get-from-inventory")
    fun getDrugsFromInventory(): Call<List<Drug>>

    @GET("drug/get-from-inventory-by-id/{drugID}")
    fun getDrugsFromInventoryByID(@Path("drugID") drugID: Int): Call<Drug>

    @GET("drug/get-from-inventory-by-category/{categoryID}/{subCategoryID}")
    fun getDrugsFromInventoryByCategory(
        @Path("categoryID") categoryID: Int,
        @Path("subCategoryID") subCategoryID: Int
    ): Call<List<Drug>>

    @GET("drug/{searchQuery}")
    fun searchDrug(
        @Path("searchQuery") searchQuery:String
    ):Call<List<Drug>>

//    @Multipart
//    @POST("drug/upload-prescription")
//    fun uploadPrescription(
//        @Part part: MultipartBody.Part
//    ): Call<ApiResponse>

    //This repository or datasource code
//    suspend fun upload(inputStream: InputStream) {
//        val part = MultipartBody.Part.createFormData(
//            "pic", "myPic", RequestBody.create(
//                MediaType.parse("image/*"),
//                inputStream.readBytes()
//            )
//        )
//        uploadPicture(part)
//    }


    //This code picks and input stream from the viewmodel or the activity itself
//    private fun pickImage() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_PHOTO)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
//            try {
//                data?.let {
//                    val inputStream: InputStream? =
//                        context?.contentResolver?.openInputStream(it.data!!)
//                    inputStream?.let { stream ->
//                        itemViewModel.uploadPicture(stream)
//                    }
//                }
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    companion object {
//        const val PICK_PHOTO = 1
//    }

    //This code will convert the File path into an  InputStream

//    var inputStream: InputStream = Files.newInputStream(Path)

}