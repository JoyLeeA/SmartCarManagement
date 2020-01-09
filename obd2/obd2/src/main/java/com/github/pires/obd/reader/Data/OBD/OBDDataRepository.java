package com.github.pires.obd.reader.Data.OBD;

import com.github.pires.obd.reader.Util.ObdCommandJob;

import java.util.ArrayList;

public class OBDDataRepository implements OBDPublisher {

    //싱글톤 패턴
    private static OBDDataRepository instance;
    private ArrayList<OBDDataModel> data = new ArrayList<>();
    private ArrayList<OBDObserver> observers = new ArrayList<>();
    private OBDDataModel model = new OBDDataModel();


    public static OBDDataRepository getInstance() {

        if(instance == null)
            instance = new OBDDataRepository();

        return instance;
    }


    public ArrayList<OBDDataModel> getData(){
        return data;
    }


    public void addData(ObdCommandJob job){


        final String cmdName = job.getCommand().getName();
        String value = "";


        if (job.getState().equals(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR)) {
            value = job.getCommand().getResult();
        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE)) {

        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED)) {

        } else {
            value = job.getCommand().getFormattedResult();

        }

        if(value.equals("NODATA")){
            return;
        }

        switch(cmdName){

            case "Engine RPM" :
                model.setRpm(value.substring(0,value.length()-3));
                break;

            case "Engine Load" :
                model.setEngineLoad(value.substring(0,value.length()-1));
                break;

            case "Engine Coolant Temperature" :
                model.setCoolantTemperature(value.substring(0,value.length()-1));
                break;

            case "Vehicle Speed" :
                model.setSpeed(value.substring(0,value.length()-4));
                break;

            case "Control Module Power Supply " :
                model.setVoltage(value.substring(0,value.length()-1));
                break;


        }


        if(model.getRpm() != null && model.getEngineLoad() != null && model.getCoolantTemperature() != null && model.getSpeed() != null && model.getVoltage() != null){

            data.add(model);
            notifyObserver(model);
            model = new OBDDataModel();

        }

    }


    @Override
    public void add(OBDObserver obdObserver) {
        observers.add(obdObserver);
    }

    @Override
    public void notifyObserver(OBDDataModel data) {

        for(int i=0;i<observers.size();i++)
            observers.get(i).update(data);

    }


}
