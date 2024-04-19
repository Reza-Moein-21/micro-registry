package com.gmail.rezamoeinpe.microregistrycommon.test.model;

import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceModelValidationException;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceModelValidationException.ServiceModelValidationError.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceModelValidationTest {

    @Test
    void validServiceModel() {
        var validModel = new ServiceModel("my-service-name", "prod-machine", 433, "https://prod-machine/health");
        assertEquals(validModel.serviceName(), "my-service-name");
        assertEquals(validModel.host(), "prod-machine");
        assertEquals(validModel.port(), 433);
        assertEquals(validModel.entry(), "https://prod-machine/health");
        assertArrayEquals(validModel.byteInfo(), "{my-service-name,prod-machine,433,https://prod-machine/health}".getBytes(StandardCharsets.US_ASCII));
    }

    @Nested
    class ServiceNameValidationTest {

        @Test
        void givingNullServiceName() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel(null, "hostname", 10));
            assertEquals(e.getCode(), SERVICE_NAME_REQUIRED.name());
        }

        @Test
        void givingNonUSASCIIServiceName() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("نام سرویس", "hostname", 10));
            assertEquals(e.getCode(), NON_US_ASCII_SERVICE_NAME.name());
        }

        @Test
        void serviceNameNotStartWithLetter() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("90ServiceName", "hostname", 1023));
            assertEquals(e.getCode(), INVALID_SERVICE_NAME_BEGINNING.name());
        }

        @Test
        void serviceNameMustOnlyIncludeLetterNumberOrDash() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("Service#Name", "hostname", 1023));
            assertEquals(e.getCode(), INVALID_SERVICE_NAME.name());
        }

        @Test
        void serviceNameMaximumLengthIs30() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("The-maximum-of-service-name-must-be-less-the-thirty", "hostname", 1023));
            assertEquals(e.getCode(), MAX_SERVICE_NAME_EXCEEDED.name());
        }

        @Test
        void serviceNameMinimumLengthIs2() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("s", "hostname", 1023));
            assertEquals(e.getCode(), MIN_SERVICE_NAME_NOT_PROVIDED.name());
        }

    }


    @Nested
    class HostValidationTest {

        @Test
        void givingNullHost() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("service-name", null, 1032));
            assertEquals(e.getCode(), HOST_REQUIRED.name());
        }

        @Test
        void givingNonUSASCIIServiceName() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("a-service-name", "هاست", 10));
            assertEquals(e.getCode(), NON_US_ASCII_HOST.name());
        }

        @Test
        void hostMaximumLengthIs30() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("a-service-name", "The-maximum-of-host-must-be-less-the-thirty", 1023));
            assertEquals(e.getCode(), MAX_HOST_EXCEEDED.name());
        }

        @Test
        void hostMinimumLengthIs2() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("my-service", "h", 8080));
            assertEquals(e.getCode(), MIN_HOST_NOT_PROVIDED.name());
        }

    }

    @Nested
    class PortValidationTest {

        @Test
        void portMaximumNumberIs65535() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("a-service-name", "my-host", 908776));
            assertEquals(e.getCode(), MAX_PORT_EXCEEDED.name());
        }

        @Test
        void portMinimumNumberIs1() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("my-service", "my-host", 0));
            assertEquals(e.getCode(), MIN_PORT_NOT_PROVIDED.name());
        }

    }

    @Nested
    class EntryValidationTest {

        @Test
        void allowToSetNull() {
            var modeWithNoEntry = new ServiceModel("my-service-name", "my-host", 1010);
            assertNull(modeWithNoEntry.entry());
        }

        @Test
        void entryMaximumLengthIs30() {
            var e = assertThrows(ServiceModelValidationException.class, () -> new ServiceModel("a-service-name", "my-host", 1023,
                    "The-maximum-of-entry-value-must-be-less-the-a-hundred_The-maximum-of-entry-value-must-be-less-the-a-hundred"));
            assertEquals(e.getCode(), MAX_ENTRY_EXCEEDED.name());
        }

    }


}
