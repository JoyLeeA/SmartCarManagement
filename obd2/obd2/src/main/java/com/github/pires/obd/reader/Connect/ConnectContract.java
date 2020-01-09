package com.github.pires.obd.reader.Connect;

import android.app.Activity;

public interface ConnectContract {

    interface View{
        void finishActivityView();
    }

    interface  Presenter{

        void attachView(ConnectContract.View view);
        void initialize(Activity activity);
        void attachAdapterModel(ConnectAdapterContract.Model adapterModel);
        void attachAdapterView(ConnectAdapterContract.View adapterView);

    }
}
