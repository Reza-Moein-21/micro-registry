package com.gmail.rezamoeinpe.microregistrycommon.test.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.RequestException;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.AbstractRequest;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.RequestFactory;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatRequest;
import com.gmail.rezamoeinpe.microregistrycommon.utils.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequestParsingFactoryTest {

    RequestFactory factory;

    @BeforeEach
    void setUp() {
        factory = new RequestFactory();
    }

    @Test
    void givingNull_shouldGetBadRequest() {
        var e = assertThrows(RequestException.class, () -> factory.byParsing(null));
        assertEquals(RequestException.RequestError.BAD_REQUEST.name(), e.getCode());
    }

    @Test
    void givingNotSupportedRequest_mustGetNotSupported() {
        var e = assertThrows(RequestException.class, () -> factory.byParsing(IOUtils.toChannel("NotSupported[]")));
        assertEquals(RequestException.RequestError.NOT_SUPPORTED.name(), e.getCode());
    }

    @Test
    void givingValidHeartBeatRequest_mustGetHeartBeatRequest() {
        AbstractRequest request = factory.byParsing(IOUtils.toChannel("HeartBeat[{my-service,green-host,443,/info}]"));
        assertInstanceOf(HeartBeatRequest.class, request);

        ServiceModel expectedModel = ((HeartBeatRequest) request).getModel();
        assertEquals("my-service", expectedModel.serviceName());
        assertEquals("green-host", expectedModel.host());
        assertEquals(443, expectedModel.port());
        assertEquals("/info", expectedModel.entry());
    }
}
