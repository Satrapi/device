package com.artronics.sdwn.device.buffer;

import java.io.InputStream;

public interface BufferDistributor
{
    void setInput(InputStream input);

    void bufferReceived();
}
