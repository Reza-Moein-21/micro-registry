package com.gmail.rezamoeinpe.microregistryservice.test;

import com.gmail.rezamoeinpe.microregistryservice.protocol.HeartBeatException;
import com.gmail.rezamoeinpe.microregistryservice.protocol.HeartBeatParser;
import com.gmail.rezamoeinpe.microregistryservice.protocol.HeartBeatParserError;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HeartBeatParserTest {

    @Test
    void nullRequest() {
        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(null));
        assertEquals(e.getError(), HeartBeatParserError.SERVER_ERROR);

    }

    @Test
    void invalidStartToken() {

        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("")));
        assertEquals(e.getError(), HeartBeatParserError.INVALID_START_TOKEN);

        e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("_")));
        assertEquals(e.getError(), HeartBeatParserError.INVALID_START_TOKEN);

    }

    @Test
    void invalidServiceNameBeginningCharacter() {
        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("[0ser],")));
        assertEquals(e.getError(), HeartBeatParserError.INVALID_SERVICE_NAME_BEGINNING);
    }

    @Test
    void onlyEnglishLetterNumberAndDash() {
        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("[serv!ce],")));
        assertEquals(e.getError(), HeartBeatParserError.INVALID_SERVICE_NAME);
    }

    @Test
    void maxServiceNameLengthExcited() {
        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("[service-name-length-should-not-be-that-much-big],")));
        assertEquals(e.getError(), HeartBeatParserError.MAX_SERVICE_NAME);
    }

    @Test
    void minServiceNameLength() {
        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("[s]")));
        assertEquals(e.getError(), HeartBeatParserError.MIN_SERVICE_NAME);
    }

    @Test
    void endTokenNotProvided() {
        var e = assertThrows(HeartBeatException.class, () -> HeartBeatParser.parser(toChannel("[valid-service-name-No-ENDING")));
        assertEquals(e.getError(), HeartBeatParserError.INVALID_END_TOKEN);
    }

    @Test
    void validRequest() throws HeartBeatException {
        var serviceName = HeartBeatParser.parser(toChannel("[valid-service-name]"));
        assertEquals(serviceName, "valid-service-name");
    }

    private static ReadableByteChannel toChannel(String text) {
        return Channels.newChannel(new ByteArrayInputStream(text.getBytes()));
    }
}
