package com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.AbstractResponse;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public final class HeartBeatResponse extends AbstractResponse {
    public final ServiceModel service;
    public final ServiceModel.ServiceStatus serviceStatus;

    public HeartBeatResponse(ServiceModel service, ServiceModel.ServiceStatus serviceStatus) {
        super("Ok");
        this.service = service;
        this.serviceStatus = serviceStatus;
    }

    @Override
    protected byte[] getData() {
        return new StringJoiner(",", "{", "}")
                .add(this.service.serviceName())
                .add(this.serviceStatus.name())
                .toString()
                .getBytes(StandardCharsets.US_ASCII);
    }
}
