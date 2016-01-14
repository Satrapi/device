package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DeviceControllerImpl implements DeviceController
{
    private final static Logger log = Logger.getLogger(DeviceControllerImpl.class);

    @Override
    public void addPacket(PacketEntity packet)
    {
        
    }
}
