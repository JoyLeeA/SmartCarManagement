package com.github.pires.obd.reader.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.pires.obd.reader.API.APIHelper.LoginAPI;
import com.github.pires.obd.reader.API.APIHelper.SignUpAPI;
import com.github.pires.obd.reader.Data.Account.AccountDataModel;
import com.github.pires.obd.reader.Data.Account.AccountDataRepository;
import com.github.pires.obd.reader.Main.MainActivity;
import com.github.pires.obd.reader.SignUp.SignUpActivity;

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View view;


    @Override
    public void attachView(LoginContract.View view) {

        this.view =view;

    }

    @Override
    public void initialize(Activity activity) {

    }

    @Override
    public void login(final Context context, String id, String password) {

        LoginAPI.getInstance().call(context, id, password, new LoginAPI.callback() {
            @Override
            public void callbackMethod(boolean isSuccessful, AccountDataModel data) {
                if(isSuccessful){

                    AccountDataRepository.getInstance().clearData();
                    AccountDataRepository.getInstance().addData(data);

                    Toast.makeText(context,"로그인 되었습니다.",Toast.LENGTH_SHORT).show();

                    context.startActivity(new Intent(context, MainActivity.class));


                    view.finishActivityView();
                }
            }
        });


    }

    @Override
    public void signup(Context context) {
        context.startActivity(new Intent(context, SignUpActivity.class));
    }
}
