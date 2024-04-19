package com.gmail.rezamoeinpe.microregistrycommon.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatRequest;
import com.gmail.rezamoeinpe.microregistrycommon.utils.ArrayUtils;

import java.nio.charset.StandardCharsets;

public abstract sealed class AbstractRequest implements Writable permits HeartBeatRequest {
    private final String type;

    protected AbstractRequest(String type) {
        this.type = type;
    }

    protected abstract byte[] getData();

    public byte[] toByte() {
        byte[] prefixBytes = type.concat("[").getBytes(StandardCharsets.US_ASCII);
        byte[] data = this.getData() == null ? new byte[0] : this.getData();
        byte[] suffixBytes = "]".getBytes(StandardCharsets.US_ASCII);

        return ArrayUtils.joinByteArrays(prefixBytes, data, suffixBytes);
    }

    @Override
    public String toString() {
        return new String(toByte());
    }
}
