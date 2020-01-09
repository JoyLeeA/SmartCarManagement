package com.github.pires.obd.reader.SignUp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.pires.obd.reader.API.APIHelper.SignUpAPI;
import com.github.pires.obd.reader.Data.Account.AccountDataModel;
import com.github.pires.obd.reader.Data.Account.AccountDataRepository;
import com.github.pires.obd.reader.Main.MainActivity;

public class SignupPresenter implements SignUpContract.Presenter {

    private SignUpContract.View view;
    public static final int MANAGER = 1001;
    public static final int DRIVER = 1002;
    public int type =0;

    @Override
    public void attachView(SignUpContract.View view) {
        this.view = view;
    }

    @Override
    public void signup(final Context context, String id, String password, String name,String car) {

        SignUpAPI.getInstance().call(context, id, password,name, car, new SignUpAPI.callback() {
            @Override
            public void callbackMethod(boolean isSuccessful, AccountDataModel data) {

                if(isSuccessful){

                    AccountDataRepository.getInstance().clearData();
                    AccountDataRepository.getInstance().addData(data);

                    Toast.makeText(context,"회원가입 되었습니다.",Toast.LENGTH_SHORT).show();

                    view.finishActivityView();
                    context.startActivity(new Intent(context, MainActivity.class));

                }

            }
        });

    }

}
