package com.artronics.sdwn.device.connection;

import com.artronics.sdwn.controller.DeviceConnectionService;
import com.artronics.sdwn.controller.exceptions.DeviceConnectionException;
import com.artronics.sdwn.device.driver.DeviceDriver;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceConnectionServiceImpl implements DeviceConnectionService
{
    private final static Logger log = Logger.getLogger(DeviceConnectionServiceImpl.class);

    private DeviceDriver deviceDriver;

    @Override
    public void init() throws DeviceConnectionException
    {
//        deviceDriver.init();
        System.out.println("kirrrr");
    }

    @Override
    public void open()
    {
        System.out.println("kosssss");
//        deviceDriver.open();
    }

    @Override
    public void sendPacket(PacketEntity packet)
    {

    }

    @Override
    public void close()
    {

    }

    @Autowired
    public void setDeviceDriver(DeviceDriver deviceDriver)
    {
        this.deviceDriver = deviceDriver;
    }
}
