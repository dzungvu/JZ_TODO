package com.project.mobile.helper;

import com.project.mobile.model.TodoInformation;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Use to
 * Created by DzungVu on 8/12/2017.
 */

public class RealmHelper {
    private Realm realm;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public ArrayList<TodoInformation> readTodoListFromRealm(){
        ArrayList<TodoInformation> result = new ArrayList<>();
        RealmResults<TodoInformation> todoInformationRealmResults = realm.where(TodoInformation.class).findAll();
        for (TodoInformation item :
                todoInformationRealmResults) {
            result.add(item);
        }
        return result;
    }


    public void deleteStudentById(final String tag) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TodoInformation> result = realm.where(TodoInformation.class).
                        equalTo("tag", tag).findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    public void addNewObject(final TodoInformation object){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(object);
            }
        });
    }
}
