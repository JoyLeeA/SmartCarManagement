package com.github.pires.obd.reader.Util;

import android.app.Application;

import com.github.pires.obd.reader.Data.Account.AccountDataRepository;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class GlobalApplication extends Application{

    private static GlobalApplication instance;

    public static GlobalApplication getGlobalApplicationContext() {

        if (instance == null)
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;



        //Realm 초기화
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("obd.realm")
                .schemaVersion(1)
                .build();

        AccountDataRepository.getInstance().setRealm(Realm.getInstance(config));

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
