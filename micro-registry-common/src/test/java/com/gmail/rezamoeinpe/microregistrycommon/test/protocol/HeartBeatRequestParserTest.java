package com.gmail.rezamoeinpe.microregistrycommon.test.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.HeartBeatRequestParserException;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatRequest;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatRequestParser;
import com.gmail.rezamoeinpe.microregistrycommon.utils.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.gmail.rezamoeinpe.microregistrycommon.exception.HeartBeatRequestParserException.HeartBeatRequestParserError.*;
import static org.junit.jupiter.api.Assertions.*;

class HeartBeatRequestParserTest {

    HeartBeatRequestParser parser;

    @BeforeEach
    void setUp() {
        this.parser = new HeartBeatRequestParser();
    }

    @Test
    void passingNullChannel() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(null));
        assertEquals(SERVER_ERROR.name(), e.getCode());
    }

    @Test
    void invalidStartTokenEmptyString() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("")));
        assertEquals(INVALID_START_TOKEN.name(), e.getCode());
    }

    @Test
    void invalidStartEmptyModel() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("heartBeat[]")));
        assertEquals(INVALID_START_TOKEN.name(), e.getCode());
    }

    @Test
    void invalidStartTokenWrongTypeName() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("hea[]")));
        assertEquals(INVALID_START_TOKEN.name(), e.getCode());
    }

    @Test
    void invalidStartTokenTypeNotProvided() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("[{service1,host-name,987}]")));
        assertEquals(INVALID_START_TOKEN.name(), e.getCode());
    }

    @Test
    void invalidStartTokenNoStartBrackets() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat]")));
        assertEquals(INVALID_START_TOKEN.name(), e.getCode());
    }

    @Test
    void invalidStartTokenJustEndToken() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("]")));
        assertEquals(INVALID_START_TOKEN.name(), e.getCode());
    }

    @Test
    void endingTokenNotProvided() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{my-service,green-host,443}")));
        assertEquals(INVALID_END_TOKEN.name(), e.getCode());
    }

    @Test
    void endingTokenNotProvidedAfterEntry() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{my-service,green-host,443,/info}")));
        assertEquals(INVALID_END_TOKEN.name(), e.getCode());
    }

    @Test
    void noStartCurlyBrass() {
        var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[service1,]")));
        assertEquals(INVALID_MODEL_START_TOKEN.name(), e.getCode());
    }

    @Test
    void validHeartBeat() {
        var expectedModel = parser.parser(IOUtils.toChannel("HeartBeat[{my-service,green-host,443,/info}]"));
        assertEquals("my-service", expectedModel.serviceName());
        assertEquals("green-host", expectedModel.host());
        assertEquals(443, expectedModel.port());
        assertEquals("/info", expectedModel.entry());
    }

    @Test
    void validHeartBeatNoEntry() {
        var expectedModel = parser.parser(IOUtils.toChannel("HeartBeat[{my-service,green-host,443}]"));
        assertEquals("my-service", expectedModel.serviceName());
        assertEquals("green-host", expectedModel.host());
        assertEquals(443, expectedModel.port());
        assertNull(expectedModel.entry());
    }

    @Test
    void validHeartBeatRequest() {
        ServiceModel validModel = new ServiceModel("service1", "192.168.20.3", 8080, "/health");
        var expectedModel = parser.parser(IOUtils.toChannel(new HeartBeatRequest(validModel).toByte()));
        assertEquals(validModel.serviceName(), expectedModel.serviceName());
        assertEquals(validModel.host(), expectedModel.host());
        assertEquals(validModel.port(), expectedModel.port());
        assertEquals(validModel.entry(), expectedModel.entry());
    }

    @Nested
    class ServiceNameTest {
        @Test
        void serviceNameShouldWithStartLetter() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{0Service1,]")));
            assertEquals(INVALID_SERVICE_NAME_BEGINNING.name(), e.getCode());
        }

        @Test
        void serviceNameShouldOnlyIncludeLetterNumberOrDash() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{Serv%ice1,]")));
            assertEquals(e.getCode(), INVALID_SERVICE_NAME.name());
        }

        @Test
        void maximumServiceNameExcited() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{maximum-service-name-exceeded-if-it-is-30,]")));
            assertEquals(MAX_SERVICE_NAME.name(), e.getCode());
        }

        @Test
        void minimumServiceNameNotProvided() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{S,]")));
            assertEquals(MIN_SERVICE_NAME.name(), e.getCode());
        }
    }

    @Nested
    class HostTest {
        @Test
        void maximumHostNotExcited() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{service1,maximum-host-name-exceeded-if-it-is-30,]")));
            assertEquals(MAX_HOST_NAME.name(), e.getCode());
        }

        @Test
        void minimumHostNotProvided() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{Service2,h,]")));
            assertEquals(MIN_HOST_NAME.name(), e.getCode());
        }
    }

    @Nested
    class PortTest {
        @Test
        void portShouldOnlyIncludeNumber() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{Service1,my-host,2q]")));
            assertEquals(e.getCode(), INVALID_PORT.name());
        }

        @Test
        void maximumPortNumberExcited() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{service1,my-host,999999}]")));
            assertEquals(MAX_PORT_NUMBER.name(), e.getCode());
        }

        @Test
        void minimumPortNumberNotProvided() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{Service2,host,0}]")));
            assertEquals(MIN_PORT_NUMBER.name(), e.getCode());
        }
    }

    @Nested
    class EntryTest {

        @Test
        void maximumEntryExcited() {
            var e = assertThrows(HeartBeatRequestParserException.class, () -> parser.parser(IOUtils.toChannel("HeartBeat[{service1,my-host,9091,/The-maximum-of-entry-value-must-be-less-the-a-hundred_The-maximum-of-entry-value-must-be-less-the-a-hundred}]")));
            assertEquals(MAX_ENTRY.name(), e.getCode());
        }
    }


}