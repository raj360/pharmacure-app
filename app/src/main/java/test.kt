//@file:Suppress("UNCHECKED_CAST")
//
//package com.pharmacure.online_pharmacy_app
//
//
//import com.pharmacure.online_pharmacy_app.viewobjects.Customer
//
//
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.Types
//import java.lang.reflect.Type
//
//
//fun main() {
//
//    // Moshi object
//    val moshi = Moshi.Builder().build()
//
//    //adapter for moshi
//    val type: Type = Types.newParameterizedType(
//        MutableList::class.java,
//        Person::class.java
//    )
//    val adapter = moshi.adapter<List<*>>(type)
//
////    val st = "{\n" +
////            "  \"id\": 1648093,\n" +
////            "  \"txRef\": \"**************************************\",\n" +
////            "  \"orderRef\": \"#####################################\",\n" +
////            "  \"flwRef\": \"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\",\n" +
////            "  \"redirectUrl\": \"http://127.0.0\",\n" +
////            "  \"device_fingerprint\": \"0000000000000000000000000\",\n" +
////            "  \"settlement_token\": null,\n" +
////            "  \"cycle\": \"one-time\",\n" +
////            "  \"amount\": 5000,\n" +
////            "  \"charged_amount\": 5000,\n" +
////            "  \"appfee\": 150,\n" +
////            "  \"merchantfee\": 0,\n" +
////            "  \"merchantbearsfee\": 1,\n" +
////            "  \"chargeResponseCode\": \"00\",\n" +
////            "  \"raveRef\": null,\n" +
////            "  \"chargeResponseMessage\": \"Pending Payment Validation\",\n" +
////            "  \"authModelUsed\": \"MOBILEMONEY\",\n" +
////            "  \"currency\": \"UGX\",\n" +
////            "  \"IP\": \"::ffff:10.71.137.172\",\n" +
////            "  \"narration\": \"Pharmacure Online medical centre \",\n" +
////            "  \"status\": \"successful\",\n" +
////            "  \"modalauditid\": \"b9353177a62bdf6c9897fc88a1ea39f6\",\n" +
////            "  \"vbvrespmessage\": \"N/A\",\n" +
////            "  \"authurl\": \"NO-URL\",\n" +
////            "  \"vbvrespcode\": \"N/A\",\n" +
////            "  \"acctvalrespmsg\": \"Approved\",\n" +
////            "  \"acctvalrespcode\": \"00\",\n" +
////            "  \"paymentType\": \"mobilemoneyug\",\n" +
////            "  \"paymentPlan\": null,\n" +
////            "  \"paymentPage\": null,\n" +
////            "  \"paymentId\": \"N/A\",\n" +
////            "  \"fraud_status\": \"ok\",\n" +
////            "  \"charge_type\": \"normal\",\n" +
////            "  \"is_live\": 0,\n" +
////            "  \"retry_attempt\": null,\n" +
////            "  \"getpaidBatchId\": null,\n" +
////            "  \"createdAt\": \"2020-10-23T09:47:29.000Z\",\n" +
////            "  \"updatedAt\": \"2020-10-23T09:47:33.000Z\",\n" +
////            "  \"deletedAt\": null,\n" +
////            "  \"customerId\": 511647,\n" +
////            "  \"AccountId\": 212039,\n" +
////            "  \"customer.id\": 511647,\n" +
////            "  \"customer.phone\": \"0787292442\",\n" +
////            "  \"customer.fullName\": \"Raymond Kalumba\",\n" +
////            "  \"customer.customertoken\": null,\n" +
////            "  \"customer.email\": \"rayjxclusive@gmail.com\",\n" +
////            "  \"customer.createdAt\": \"2020-10-22T18:15:30.000Z\",\n" +
////            "  \"customer.updatedAt\": \"2020-10-22T18:15:30.000Z\",\n" +
////            "  \"customer.deletedAt\": null,\n" +
////            "  \"customer.AccountId\": 212039,\n" +
////            "  \"meta\": [ ],\n" +
////            "  \"flwMeta\": { }\n" +
////            "}"
//
//
//    val customer =   Customer(customerID=24, fullName="Raymond Kalumba Joseph", telephone="+256787292442", email="rayjxclusive@gmail.com", token="https://lh3.googleusercontent.com/a-/AOh14GjHtDHDnD92Gxqjn9j6ztfAV_LgG-uni7fzBvOfrg=s96-c", dateOfBirth="2020-01-01T00:00:00.000Z")
//
//
//    val raveResponse = "{\n" +
//            "  \"status\": \"success\",\n" +
//            "  \"message\": \"Tx Fetched\",\n" +
//            "  \"data\": {\n" +
//            "    \"id\": 1648093,\n" +
//            "    \"txRef\": \"8f2760b7-9387-44f1-8708-4fa3699ff9ca\",\n" +
//            "    \"orderRef\": \"URF_MMGH_1603446449335_5149635\",\n" +
//            "    \"flwRef\": \"flwm3s4m0c1603446449876\",\n" +
//            "    \"redirectUrl\": \"http://127.0.0\",\n" +
//            "    \"device_fingerprint\": \"0316067d21c8575e\",\n" +
//            "    \"settlement_token\": null,\n" +
//            "    \"cycle\": \"one-time\",\n" +
//            "    \"amount\": 5000,\n" +
//            "    \"charged_amount\": 5000,\n" +
//            "    \"appfee\": 150,\n" +
//            "    \"merchantfee\": 0,\n" +
//            "    \"merchantbearsfee\": 1,\n" +
//            "    \"chargeResponseCode\": \"00\",\n" +
//            "    \"raveRef\": null,\n" +
//            "    \"chargeResponseMessage\": \"Pending Payment Validation\",\n" +
//            "    \"authModelUsed\": \"MOBILEMONEY\",\n" +
//            "    \"currency\": \"UGX\",\n" +
//            "    \"IP\": \"::ffff:10.71.137.172\",\n" +
//            "    \"narration\": \"Pharmacure Online medical centre \",\n" +
//            "    \"status\": \"successful\",\n" +
//            "    \"modalauditid\": \"b9353177a62bdf6c9897fc88a1ea39f6\",\n" +
//            "    \"vbvrespmessage\": \"N/A\",\n" +
//            "    \"authurl\": \"NO-URL\",\n" +
//            "    \"vbvrespcode\": \"N/A\",\n" +
//            "    \"acctvalrespmsg\": \"Approved\",\n" +
//            "    \"acctvalrespcode\": \"00\",\n" +
//            "    \"paymentType\": \"mobilemoneyug\",\n" +
//            "    \"paymentPlan\": null,\n" +
//            "    \"paymentPage\": null,\n" +
//            "    \"paymentId\": \"N/A\",\n" +
//            "    \"fraud_status\": \"ok\",\n" +
//            "    \"charge_type\": \"normal\",\n" +
//            "    \"is_live\": 0,\n" +
//            "    \"retry_attempt\": null,\n" +
//            "    \"getpaidBatchId\": null,\n" +
//            "    \"createdAt\": \"2020-10-23T09:47:29.000Z\",\n" +
//            "    \"updatedAt\": \"2020-10-23T09:47:33.000Z\",\n" +
//            "    \"deletedAt\": null,\n" +
//            "    \"customerId\": 511647,\n" +
//            "    \"AccountId\": 212039,\n" +
//            "    \"customer.id\": 511647,\n" +
//            "    \"customer.phone\": \"0787292442\",\n" +
//            "    \"customer.fullName\": \"Raymond Kalumba\",\n" +
//            "    \"customer.customertoken\": null,\n" +
//            "    \"customer.email\": \"rayjxclusive@gmail.com\",\n" +
//            "    \"customer.createdAt\": \"2020-10-22T18:15:30.000Z\",\n" +
//            "    \"customer.updatedAt\": \"2020-10-22T18:15:30.000Z\",\n" +
//            "    \"customer.deletedAt\": null,\n" +
//            "    \"customer.AccountId\": 212039,\n" +
//            "    \"meta\": [ ],\n" +
//            "    \"flwMeta\": { }\n" +
//            "  }\n" +
//            "}"
//
////    //Create json
////    val jsonString  = "[\n" +
////            "  {\n" +
////            "    \"name\": \"Raymond Kalumba\",\n" +
////            "    \"email\": \"raymond.kalumba@gmail.com\",\n" +
////            "    \"phone\": {\n" +
////            "      \"home\": \"flwm3s4m0c1603446449876\",\n" +
////            "       \"mobile\": \"0787292442\"\n" +
////            "    }\n" +
////            "  },\n" +
////            "  {\n" +
////            "    \"name\": \"Kisitu Gideon\",\n" +
////            "    \"email\": \"kisitu.gideon@gmail.com\",\n" +
////            "    \"phone\": {\n" +
////            "      \"home\": \"b9353177a62bdf6c9897fc88a1ea39f6\",\n" +
////            "       \"mobile\": \"07328328320\"\n" +
////            "    }\n" +
////            "  }\n" +
////            "]"
//
////   val jsonArray= JsonParser().parse(jsonString).asJsonArray
////
////    val raveJsonObject = JsonParser().parse(raveResponse).asJsonObject
////
////    println()
////    println(raveJsonObject)
//
////    val raveObject = RaveResponse = adapter.fromJson(raveResponse) as RaveResponse
//
//////    //Get output
//////   val persons: List<Person> = adapter.fromJson(jsonString) as List<Person>
////
//////    print(persons)
////
////    val data = raveJsonObject.getAsJsonObject("data")
////
////
////    val txRef = data["customer.phone"]
////
////    println("txRef: $txRef")
//
//
//
////    //Use case of Kloxon
////    val result = Klaxon()
////        .parse<RaveResponse>(raveResponse)
//
////    println(result)
//
//}
//
//
//
//
//data class Person(
//    val name: String,
//    val email: String,
//    val phone: Phone
//)
//
//data class Phone(
//    val home: String,
//    val mobile: String
//)
//
//
//
