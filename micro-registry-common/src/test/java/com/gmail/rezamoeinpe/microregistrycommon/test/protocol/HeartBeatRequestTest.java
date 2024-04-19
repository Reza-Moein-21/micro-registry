package com.gmail.rezamoeinpe.microregistrycommon.test.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatRequest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


class HeartBeatRequestTest {

    @Test
    void givingNullModel() {
        var req = new HeartBeatRequest(null);
        assertArrayEquals(req.toByte(), "HeartBeat[]".getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    void givingValidModel() {
        ServiceModel model = new ServiceModel("my-service", "my-host", 2030);
        var req = new HeartBeatRequest(model);
        assertArrayEquals(req.toByte(), "HeartBeat[".concat(new String(model.byteInfo())).concat("]").getBytes(StandardCharsets.US_ASCII));
    }
}