package com.artronics.sdwn.device.driver;


import com.artronics.sdwn.controller.exceptions.DeviceConnectionException;

public interface DeviceDriver
{
    void init();

    void open() throws DeviceConnectionException;

    void close() throws DeviceConnectionException;
}
