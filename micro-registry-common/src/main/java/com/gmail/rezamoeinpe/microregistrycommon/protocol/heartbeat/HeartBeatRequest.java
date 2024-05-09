package com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.AbstractRequest;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;

public final class HeartBeatRequest extends AbstractRequest {

    private final ServiceModel model;

    public HeartBeatRequest(ServiceModel model) {
        super("HeartBeat");
        this.model = model;
    }

    @Override
    protected byte[] getData() {
        if (model == null) return null;
        return model.byteInfo();
    }

    public ServiceModel getModel() {
        return this.model;
    }
}
