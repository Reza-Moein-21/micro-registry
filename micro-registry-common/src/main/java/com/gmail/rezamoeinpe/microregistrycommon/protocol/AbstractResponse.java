package com.gmail.rezamoeinpe.microregistrycommon.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatResponse;
import com.gmail.rezamoeinpe.microregistrycommon.utils.ArrayUtils;

import java.nio.charset.StandardCharsets;

public abstract sealed class AbstractResponse implements Writable permits HeartBeatResponse {
    private final String status;

    protected AbstractResponse(String status) {
        this.status = status;
    }

    protected abstract byte[] getData();

    public byte[] toByte() {
        byte[] prefixBytes = "[".getBytes(StandardCharsets.US_ASCII);
        byte[] data = this.getData() == null ? new byte[0] : this.getData();
        byte[] suffixBytes = "]".concat(status).getBytes(StandardCharsets.US_ASCII);

        return ArrayUtils.joinByteArrays(prefixBytes, data, suffixBytes);
    }

    @Override
    public String toString() {
        return new String(toByte());
    }
}
