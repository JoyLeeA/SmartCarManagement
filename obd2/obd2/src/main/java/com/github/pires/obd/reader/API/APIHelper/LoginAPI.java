package com.github.pires.obd.reader.API.APIHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.pires.obd.reader.API.APIClient;
import com.github.pires.obd.reader.API.APIInterface;
import com.github.pires.obd.reader.API.Retrofit;
import com.github.pires.obd.reader.Data.Account.AccountDataModel;
import com.github.pires.obd.reader.Util.CheckNetwork;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class LoginAPI {

    private static LoginAPI instance;

    public static LoginAPI getInstance() {
        if(instance == null){
            instance = new LoginAPI();
        }

        return instance;
    }

    public interface callback{
        void callbackMethod(boolean isSuccessful, AccountDataModel data);
    }

    public void call(final Context context, String id, String password, final LoginAPI.callback callback){

        if(!CheckNetwork.getInstance().isNetworkAvailable(context)){
            Toast.makeText(context,"네트워크를 확인해 주세요.",Toast.LENGTH_SHORT).show();
            callback.callbackMethod(false,null);
        }
        else{

            final ProgressDialog progressDialog = ProgressDialog.show(context, "로그인중..", "잠시만 기다려 주세요.", true, false);

            APIInterface apiInterface = APIClient.getBaseClient().create(APIInterface.class);

            final JSONObject body = new JSONObject();

            try {
                body.put("id", id);
                body.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Call<String> call = apiInterface.login(body.toString());

            Retrofit.getInstance().Enqueue(context, call, new Retrofit.callback() {
                @Override
                public void onResponseCallback(Response<String> response) {

                    if(response.isSuccessful()){

                        Gson gson = new Gson();
                        callback.callbackMethod(true,gson.fromJson(response.body().toString(),AccountDataModel.class));

                    }
                    else{

                        Toast.makeText(context,response.errorBody().toString(),Toast.LENGTH_SHORT).show();

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
