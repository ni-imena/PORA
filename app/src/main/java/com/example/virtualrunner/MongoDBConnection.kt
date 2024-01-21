package com.example.virtualrunner

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.FindPublisher
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document

class MongoDBConnection (private val connectionString: String, private val databaseName: String) {
    private var mongoClient = MongoClients.create(connectionString)
    private var database: MongoDatabase = mongoClient.getDatabase(databaseName)

//    private fun createMongoClient(connectionString: String): com.mongodb.client.MongoClient {
//        val clientSettings = MongoClientSettings.builder()
//            .applyConnectionString(ConnectionString(connectionString))
//            .applyToDnsResolver { it.dnsResolver(com.mongodb.internal.dns.SystemPropertyDnsResolver()) }
//            .build()
//
//        return MongoClients.create(clientSettings)
//    }

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

    // TEST
//    fun findFirstDocument(collectionName: String, filter: Document): Publisher<Document> {
//        val collection = getCollection(collectionName)
//        return collection.find().first()
//    }

    // Add more query functions as needed (update, delete, etc.)
}

//FOR TESTING ONLY -------- USE THE CLASS WHERE NEEDED
fun main() {
    val connectionString = "mongodb+srv://admin:admin@ni-imena.sygmxf2.mongodb.net/?retryWrites=true&w=majority"
    val databaseName = "ni_imena"

    // Create MongoDBConnection instance
    val mongoDBConnection = MongoDBConnection(connectionString, databaseName)

    try {
        // Example usage
        val collectionName = "runs"
        //val document = Document("key", "value")

        // Insert document
        //mongoDBConnection.insertDocument(collectionName, document)

        // Find documents
//        val filter = Document("key", "value")
//        val firstDocument = mongoDBConnection.findFirstDocument(collectionName, filter)
//
//        if (firstDocument != null) {
//            println("First Document: $firstDocument")
//        } else {
//            println("No matching documents found.")
//        }

        // Get the count of documents in the collection
        val count = mongoDBConnection.getCollection(collectionName).countDocuments()

        // Output the count
        println("Number of documents in '$collectionName': $count")

    } finally {
        // Close the connection in a finally block to ensure it's closed even if an exception occurs
        mongoDBConnection.closeConnection()
    }
}