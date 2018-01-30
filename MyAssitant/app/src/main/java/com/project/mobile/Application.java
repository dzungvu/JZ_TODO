package com.project.mobile;

import android.content.Context;

import com.project.mobile.utils.MyRealmMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by DzungVu on 8/12/2017.
 */

public class Application extends android.app.Application {
    private final String REALM_NAME = "myassistant";
    private final int SCHEMA_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)
                .migration(new MyRealmMigration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
