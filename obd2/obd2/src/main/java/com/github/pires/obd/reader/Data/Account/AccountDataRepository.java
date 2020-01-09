package com.github.pires.obd.reader.Data.Account;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;

public class AccountDataRepository {

    private static AccountDataRepository instance;
    private AccountDataModel data;
    private Realm realm;

    public static AccountDataRepository getInstance() {

        if(instance == null)
            instance = new AccountDataRepository();

        return instance;
    }


    public void addData(AccountDataModel data){

        realm.beginTransaction();

        realm.copyToRealm(data);

        realm.commitTransaction();

    }

    public void clearData(){

        realm.beginTransaction();

        AccountDataModel d = realm.where(AccountDataModel.class).findFirst();

        if(d !=null)
         d.deleteFromRealm();

        realm.commitTransaction();

    }


    public void setRealm(Realm realm){
        this.realm = realm;
    }

    public AccountDataModel getData(){
        return realm.where(AccountDataModel.class).findFirst();
    }




}
