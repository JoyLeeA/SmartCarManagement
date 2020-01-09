package com.github.pires.obd.reader.Data.OBD;

public interface OBDPublisher {
    void add(OBDObserver obdObserver);
    void notifyObserver(OBDDataModel data);
}
