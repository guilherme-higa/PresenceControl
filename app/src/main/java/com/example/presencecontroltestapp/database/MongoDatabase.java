package com.example.presencecontroltestapp.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;
import com.example.presencecontroltestapp.ui.fragments.ClassInformation;
import com.example.presencecontroltestapp.utils.Constants;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MongoDatabase  {
    private static final String TAG = MongoDatabase.class.getSimpleName();
    private Context mContext;
    private IDatabaseResult mCallBack;
    private io.realm.mongodb.mongo.MongoDatabase mMongoDatabase;
    private MongoCollection<Document> mMongoCollection;
    private MongoCollection<Document> mMongoCollectionForClass;
    private MongoClient mMongoClient;
    private App mApp;
    private User mUser;

    public MongoDatabase(Context context, IDatabaseResult callback) {
        mContext = context;
        mCallBack = callback;
        mApp = new App(new AppConfiguration.Builder(Constants.appId).build());
        mUser = mApp.currentUser();
            mMongoClient = mUser.getMongoClient(Constants.serviceName);
            mMongoDatabase= mMongoClient.getDatabase(Constants.databaseName);
            mMongoCollection = mMongoDatabase.getCollection(Constants.mCollectionName);
            mMongoCollectionForClass = mMongoDatabase.getCollection(Constants.mClassInformationCollection);
    }

    public void connectToMongoDB() {
        Credentials credentials = Credentials.emailPassword(Constants.credentialEmail, Constants.mongoCredentialPassword);
        mApp.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                mCallBack.onConnectionResponse(result.isSuccess());
            }
        });
    }

    public synchronized void select(int ra, String password) {
        Document queryFilter = new Document().append(Constants.credentialRa, ra).append(
                Constants.credentialPassword, password);
        mMongoCollection.findOne(queryFilter).getAsync(result -> {
            Document document = result.get();
            if (result.isSuccess() && document != null) {
                Toast.makeText(mContext, mContext.getString(R.string.user_found), Toast.LENGTH_SHORT).show();
                Students students = new Students(String.valueOf(document.get(Constants.name)),
                        String.valueOf(document.get(Constants.email)),
                        Integer.parseInt(String.valueOf(document.get(Constants.credentialRa))),
                        String.valueOf(document.get(Constants.credentialPassword)));
                mCallBack.onSelectResponse(true, students);
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                mCallBack.onSelectResponse(false);
            }
        });
    }


    public synchronized void selectFrequency(IDatabaseResult.RecoveryFrequency callback, int ra, String materia, String data) {
        Document queryFilter = new Document().append(Constants.credentialRa, ra).append(
                Constants.name, materia).append(Constants.diaDaSemana, data);
        mMongoCollectionForClass.findOne(queryFilter).getAsync(result -> {
            Document document = result.get();
            if (result.isSuccess() && document != null) {
                ClassInformation classInformation = new ClassInformation(String.valueOf(document.get(Constants.name)),
                        String.valueOf(document.get(Constants.professorName)),
                        Integer.parseInt(String.valueOf(document.get(Constants.qtdAulasDadas))),
                        Integer.parseInt(String.valueOf(document.get(Constants.qtdAulasAssistidas))),
                        String.valueOf(document.get(Constants.diaDaSemana)));
                callback.onRecoveryFrequency(true, classInformation);
            } else {
                callback.onRecoveryFrequency(false);
            }
        });
    }

    public synchronized void selectClassInformation(IDatabaseResult.informationClass callback ,int ra, String dia) {
        Document queryFilter = new Document().append(Constants.credentialRa, ra).append(Constants.diaDaSemana, dia);
        RealmResultTask<MongoCursor<Document>> findTask = mMongoCollectionForClass.find(queryFilter).iterator();
        findTask.getAsync(result -> {
            if (result.isSuccess()) {
                MongoCursor<Document> results = result.get();
                List<ClassInformation> list = new ArrayList<>();
                ClassInformation classInformation;
                while (results.hasNext()) {
                    Document currentDocument = results.next();
                    classInformation = new ClassInformation(String.valueOf(currentDocument.get(Constants.name)),
                            String.valueOf(currentDocument.get(Constants.professorName)),
                            Integer.parseInt(String.valueOf(currentDocument.get(Constants.qtdAulasDadas))),
                            Integer.parseInt(String.valueOf(currentDocument.get(Constants.qtdAulasAssistidas))),
                            String.valueOf(currentDocument.get(Constants.diaDaSemana)));
                    list.add(classInformation);
                }
                callback.onClassInformationSelect(true, list);
            }else {
                callback.onClassInformationSelect(false);
            }
        });
    }


    public synchronized void findAndUpdateFrequency(int ra, String dia, String materia, int newFrequency, int newClassNumber) {
        Document queryFilter = new Document().append(Constants.credentialRa, ra).append(Constants.diaDaSemana, dia).append(Constants.name, materia);
        Document updateDocument = new Document("$set", new Document(Constants.qtdAulasAssistidas, newFrequency).append(Constants.qtdAulasDadas, newClassNumber));
        mMongoCollectionForClass.findOneAndUpdate(queryFilter, updateDocument).getAsync(result -> {
            Document document = result.get();
            if (result.isSuccess() && document != null) {
                Toast.makeText(mContext, mContext.getString(R.string.frequency_updated), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.frequency_update_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public synchronized void selectClassInformation(IDatabaseResult.informationClass callback ,int ra) {
//        Document queryFilter = new Document().append(Constants.credentialRa, ra);
//        mMongoCollectionForClass.findOne(queryFilter).getAsync(result -> {
//            Document document = result.get();
//            if (result.isSuccess() && document != null) {
//                ClassInformation classInformation = new ClassInformation(String.valueOf(document.get(Constants.name)),
//                        String.valueOf(document.get(Constants.professorName)),
//                        Integer.parseInt(String.valueOf(document.get(Constants.qtdAulasDadas))),
//                        Integer.parseInt(String.valueOf(document.get(Constants.qtdAulasAssistidas))));
//                List<ClassInformation> list = new ArrayList<>();
//                list.add(classInformation);
//                callback.onClassInformationSelect(true, list);
//            } else {
//                callback.onClassInformationSelect(false);
//            }
//        });
//    }

    public synchronized void selectRecovery(IDatabaseResult.Recovery callback, int ra, String email) {
        Document queryFilter = new Document().append(Constants.credentialRa, ra).append(Constants.email, email);
        mMongoCollection.findOne(queryFilter).getAsync(result -> {
            Document document = result.get();
            callback.onRecoverySelect(result.isSuccess() && document != null);
        });
    }

    public synchronized void selectCreateAccount(IDatabaseResult.CreateAccount callback, int ra) {
        Document queryFilter = new Document().append(Constants.credentialRa, ra);
        mMongoCollection.findOne(queryFilter).getAsync(result -> {
            Document document = result.get();
            callback.onCreateAccountSelect(result.isSuccess() && document != null);
        });
    }

    public synchronized void  insert (Students students) {
        mMongoCollection.insertOne(new Document(Constants.credentialPassword,
                students.getCredentialsPassword()).append(
                Constants.credentialRa, students.getCredentialsRa()).append(Constants.email,
                students.getEmail()).append(Constants.name, students.getName())).getAsync(result1 -> {
            Toast.makeText(mContext, result1.isSuccess() ? mContext.getString(
                    R.string.user_successfully_created) :
                    mContext.getString(
                            R.string.user_failed_created), Toast.LENGTH_SHORT).show();
        });
    }

    public synchronized void update(int ra, String password) {
        Document queryFilter = new Document(Constants.credentialRa, ra);
        Document updateDocument = new Document("$set", new Document(Constants.credentialPassword, password));
        mMongoCollection.updateOne(queryFilter, updateDocument).getAsync(task -> {
            Toast.makeText(mContext, task.isSuccess() ? mContext.getString(
                    R.string.password_updated_successful) :
                    mContext.getString(
                            R.string.password_updated_failed), Toast.LENGTH_SHORT).show();
        });
    }

    public synchronized void delete(Integer ra) {
        Document queryFilter = new Document(Constants.credentialRa, ra);
        mMongoCollection.deleteOne(queryFilter).getAsync(task -> {
            Toast.makeText(mContext, task.isSuccess() ? mContext.getString(
                    R.string.delete_user_successfully) :
                    mContext.getString(
                            R.string.delete_user_failed), Toast.LENGTH_SHORT).show();
        });
    }

}
