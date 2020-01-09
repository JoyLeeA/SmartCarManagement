package com.github.pires.obd.reader.API.APIHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.github.pires.obd.reader.API.APIClient;
import com.github.pires.obd.reader.API.APIInterface;
import com.github.pires.obd.reader.API.Retrofit;
import com.github.pires.obd.reader.Data.Account.AccountDataModel;
import com.github.pires.obd.reader.R;
import com.github.pires.obd.reader.Util.CheckNetwork;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpAPI {

    private static SignUpAPI instance;

    public static SignUpAPI getInstance() {
        if(instance == null){
            instance = new SignUpAPI();
        }

        return instance;
    }

    public interface callback{
        void callbackMethod(boolean isSuccessful, AccountDataModel data);

    }

    public void call(Context context, String id, String password,String name, String car, final callback callback){

        if(!CheckNetwork.getInstance().isNetworkAvailable(context)){
            Toast.makeText(context,"네트워크를 확인해 주세요.",Toast.LENGTH_SHORT).show();
            callback.callbackMethod(false,null);
        }
        else{

            final ProgressDialog progressDialog = ProgressDialog.show(context, "회원가입중..", "잠시만 기다려 주세요.", true, false);

            APIInterface apiInterface = APIClient.getBaseClient().create(APIInterface.class);

            final JSONObject body = new JSONObject();

            try {
                body.put("id", id);
                body.put("password", password);
                body.put("car", car);
                body.put("name",name);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Call<String> call = apiInterface.signup(body.toString());

            Retrofit.getInstance().Enqueue(context, call, new Retrofit.callback() {
                @Override
                public void onResponseCallback(Response<String> response) {

                    if(response.isSuccessful()){

                        Gson gson = new Gson();

                        try {
                            body.put("user_id",response.body().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callback.callbackMethod(true,gson.fromJson(body.toString(),AccountDataModel.class));



                    }
                    else{
                        callback.callbackMethod(false,null);
                    }

                    progressDialog.dismiss();


                }

                @Override
                public void onFailureCallback() {
                    callback.callbackMethod(false,null);
                    progressDialog.dismiss();
                }

                @Override
                public void onNetworkDisableCallback() {
                    callback.callbackMethod(false,null);
                    progressDialog.dismiss();
                }
            });




        }

    }
}
