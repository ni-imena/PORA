package com.example.virtualrunner

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.FindIterable
import com.mongodb.reactivestreams.client.FindPublisher
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.bson.types.ObjectId

class MongoDBConnection (private val connectionString: String, private val databaseName: String) {
    private var mongoClient = MongoClients.create(connectionString)
    private var database: MongoDatabase = mongoClient.getDatabase(databaseName)

    fun closeConnection() {
        mongoClient.close()
    }

    fun getCollection(collectionName: String): MongoCollection<Document> {
        return database.getCollection(collectionName)
    }

    fun insertDocument(collectionName: String, document: Document) {
        val collection = getCollection(collectionName)
        collection.insertOne(document)
    }

    fun findDocument(collectionName: String, filter: Document): FindPublisher<Document> {
        val collection = getCollection(collectionName)
        return collection.find(filter)
    }
}

// FOR TESTING ONLY -------- USE THE CLASS WHERE NEEDED
fun main() {
//    val connectionString = "mongodb+srv://admin:admin@ni-imena.sygmxf2.mongodb.net/?retryWrites=true&w=majority"
//    val databaseName = "ni_imena"
//
//    // Create MongoDBConnection instance
//    val mongoDBConnection = MongoDBConnection(connectionString, databaseName)
//
//    val collection = mongoDBConnection.getCollection("runs")
//
//    val documents: FindIterable<Document> = mongoDBConnection.findDocuments(Document("_id", ObjectId(runId)), "runs")
//    val runDocument: Document? = documents.first()
//
//    if (runDocument != null) {
//        val stream: Document? = runDocument.get("stream") as? Document
//
//        if (stream != null) {
//            val latlng: Document? = stream.get("latlng") as? Document
//
//            if (latlng != null) {
//                val data: List<List<Double>>? = latlng.get("data") as? List<List<Double>>
//
//                if (data != null) {
//                    //val runCoordinates = arrayOfNulls<Geolocation>(data.size / reduction)
//                    //val reducedCoordinates = ArrayList<Geolocation>()
//
//                    for (i in 0 until data.size step reduction) {
//                        val latlngData = data[i]
//                        reducedCoordinates.add(Geolocation(latlngData[0], latlngData[1]))
//                    }
//
//                    runCoordinates = reducedCoordinates.toTypedArray()
//                }
//            }
//        }
//    }


}
