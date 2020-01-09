package com.github.pires.obd.reader.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.pires.obd.reader.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.View,View.OnClickListener{

    private TextView login,signup;
    private EditText id,password;
    private LoginPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = findViewById(R.id.login_login);
        signup = findViewById(R.id.login_signup);
        id = findViewById(R.id.login_id);
        password = findViewById(R.id.login_password);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);

        presenter = new LoginPresenter();
        presenter.attachView(this);
        presenter.initialize(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.login_login :
                presenter.login(this,id.getText().toString(),password.getText().toString());
                break;

            case R.id.login_signup :
                presenter.signup(this);
                break;

        }

    }

    @Override
    public void finishActivityView() {
        finish();
    }
}
