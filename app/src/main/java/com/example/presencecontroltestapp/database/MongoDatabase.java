package com.example.presencecontroltestapp.database;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.content.Context;
import android.util.Log;

import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.utils.Constants;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;

public class MongoDatabase {
    private static final String TAG = MongoDatabase.class.getSimpleName();

    private Context mContext;
    private MongoCollection<Students> mMongoCollection;


    public MongoDatabase(Context context, String databaseName, String collectionName) {
        mContext = context;
        Realm.init(context);
    }

    public boolean connectToMongoDB(String databaseName, String collectionName) {
        AtomicBoolean result = new AtomicBoolean(false);
        App app = new App(new AppConfiguration.Builder(Constants.AppId).build());
        Credentials credentials = Credentials.anonymous();

        AtomicReference<User> user = new AtomicReference<>();
        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                result.set(true);
                user.set(app.currentUser());
            } else {
                Log.d(TAG, it.getError().toString());
            }
        });

        MongoClient mongoClient = user.get().getMongoClient("myAtlasCluster");
        io.realm.mongodb.mongo.MongoDatabase mongoDatabase =
                mongoClient.getDatabase(databaseName);

        CodecRegistry pojo = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        mMongoCollection = mongoDatabase.getCollection(collectionName,
                Students.class).withCodecRegistry(pojo);

        return result.get();
    }

    private boolean insertOneRow(Students students) {
        AtomicBoolean result = new AtomicBoolean(false);
        mMongoCollection.insertOne(students).getAsync(task -> {
            if (task.isSuccess()) {
                result.set(true);
            } else {
                Log.d(TAG, String.valueOf(task.getError()));
            }
        });
        return result.get();
    }

    private boolean readOneRow(Students students) {
        AtomicBoolean result = new AtomicBoolean(false);
        Document queryFilter = new Document("name", students.getName());
        mMongoCollection.findOne(queryFilter).getAsync(task -> {
            if (task.isSuccess() && task.get() != null) {
                Students stud = task.get();
                result.set(true);
                Log.d(TAG, "<---Higa---> found row : " + stud);
            } else {
                Log.d(TAG, String.valueOf(task.getError()));
            }
        });
        return result.get();
    }

    private boolean updateOneRow(Students students, int newAge) {
        AtomicBoolean result = new AtomicBoolean(false);
        Document queryFilter = new Document("name", students.getName());
        Document updateDocument = new Document("$set", new Document("age", newAge));
        mMongoCollection.updateOne(queryFilter, updateDocument).getAsync(task -> {
            if (task.isSuccess()) {
                result.set(true);
            } else {
                Log.d(TAG, String.valueOf(task.getError()));
            }
        });
        return result.get();
    }

    private boolean deleteOneRow(Students students) {
        AtomicBoolean result = new AtomicBoolean(false);
        Document queryFilter = new Document("name", students.getName());
        mMongoCollection.deleteOne(queryFilter).getAsync(task -> {
            if (task.isSuccess()) {
                result.set(true);
            } else {
                Log.d(TAG, String.valueOf(task.getError()));
            }
        });
        return result.get();
    }
}
