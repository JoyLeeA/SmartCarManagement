package com.github.pires.obd.reader.API.APIHelper;

import android.app.ProgressDialog;
import android.content.Context;

import com.github.pires.obd.reader.API.APIClient;
import com.github.pires.obd.reader.API.APIInterface;
import com.github.pires.obd.reader.API.Retrofit;
import com.github.pires.obd.reader.Data.Account.AccountDataModel;
import com.github.pires.obd.reader.Data.Account.AccountDataRepository;
import com.github.pires.obd.reader.Data.OBD.OBDDataModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class RecordAPI {

    private static RecordAPI instance;

    public static RecordAPI getInstance() {
        if(instance == null){
            instance = new RecordAPI();
        }

        return instance;
    }

    public interface callback{
        void callbackMethod(boolean isSuccessful, ArrayList<OBDDataModel> data);
    }


    public void call(Context context, String date, final callback callback){

        final ProgressDialog progressDialog = ProgressDialog.show(context, "조회중..", "잠시만 기다려 주세요.", true, false);

        APIInterface apiInterface = APIClient.getBaseClient().create(APIInterface.class);

        final JSONObject body = new JSONObject();

        try {
            body.put("user_id", AccountDataRepository.getInstance().getData().getUser_id());
            body.put("date",date);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Call<String> call = apiInterface.getObdData(body.toString());

        Retrofit.getInstance().Enqueue(context, call, new Retrofit.callback() {
            @Override
            public void onResponseCallback(Response<String> response) {

                if(response.isSuccessful()){

                    ArrayList<OBDDataModel> result = new ArrayList<>();
                    try {

                        Gson gson = new Gson();

                        JSONArray jsonArray = new JSONArray(response.body().toString());

                        for(int i=0;i<jsonArray.length();i++){

                            OBDDataModel newData = gson.fromJson(jsonArray.getString(i),OBDDataModel.class);

                            result.add(newData);

                        }

                        callback.callbackMethod(true,result);


                    } catch (JSONException e) {
                       callback.callbackMethod(false,null);
                    }


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
