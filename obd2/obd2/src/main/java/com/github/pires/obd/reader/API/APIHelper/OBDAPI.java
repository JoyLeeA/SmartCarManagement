package com.github.pires.obd.reader.API.APIHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.github.pires.obd.reader.API.APIClient;
import com.github.pires.obd.reader.API.APIInterface;
import com.github.pires.obd.reader.API.Retrofit;
import com.github.pires.obd.reader.Data.Account.AccountDataModel;
import com.github.pires.obd.reader.Data.Account.AccountDataRepository;
import com.github.pires.obd.reader.Data.OBD.OBDDataModel;
import com.github.pires.obd.reader.Util.CheckNetwork;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class OBDAPI {

    private static OBDAPI instance;

    public static OBDAPI getInstance() {
        if(instance == null){
            instance = new OBDAPI();
        }

        return instance;
    }

    public void call(Context context, OBDDataModel data){

        APIInterface apiInterface = APIClient.getBaseClient().create(APIInterface.class);

        final JSONObject body = new JSONObject();

        try {
            body.put("user_id", AccountDataRepository.getInstance().getData().getUser_id());
            body.put("speed",data.getSpeed());
            body.put("voltage",data.getVoltage());
            body.put("rpm",data.getRpm());
            body.put("cool",data.getCoolantTemperature());
            body.put("load",data.getEngineLoad());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Call<String> call = apiInterface.setObdData(body.toString());

        Retrofit.getInstance().Enqueue(context, call, new Retrofit.callback() {
            @Override
            public void onResponseCallback(Response<String> response) {


            }

            @Override
            public void onFailureCallback() {

            }

            @Override
            public void onNetworkDisableCallback() {

            }
        });

    }
}
