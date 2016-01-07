package com.artronics.sdwn.device.driver;

import com.artronics.sdwn.device.exception.DeviceConnectionException;

public interface DeviceDriver
{
    void init();

    void open() throws DeviceConnectionException;
}
