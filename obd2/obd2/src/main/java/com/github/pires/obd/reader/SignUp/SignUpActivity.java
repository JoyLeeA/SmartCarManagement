package com.github.pires.obd.reader.SignUp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.github.pires.obd.reader.R;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View,View.OnClickListener{

    private EditText id,password,name,car;
    private TextView signup;
    private SignupPresenter presenter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        id = (EditText) findViewById(R.id.signup_id);
        password = (EditText) findViewById(R.id.signup_password);
        signup = (TextView) findViewById(R.id.signup_signup);
        name = findViewById(R.id.signup_name);
        car = findViewById(R.id.signup_car);

        signup.setOnClickListener(this);

        presenter = new SignupPresenter();
        presenter.attachView(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.signup_signup :
                presenter.signup(this,id.getText().toString(),password.getText().toString(),name.getText().toString(),car.getText().toString());
                break;
        }

    }

    @Override
    public void finishActivityView() {
        finish();
    }

}
