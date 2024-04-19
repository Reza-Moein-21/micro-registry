package com.gmail.rezamoeinpe.microregistrycommon.test.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatResponse;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class HeartBeatResponseTest {

    @Test
    void validHeartBeatResponse() {
        var res = new HeartBeatResponse(new ServiceModel("my-service", "my-host", 1210), ServiceModel.ServiceStatus.ACTIVE);
        var expected = "[{my-service,ACTIVE}]Ok";
        assertArrayEquals(res.toByte(), expected.getBytes(StandardCharsets.US_ASCII));
    }
}