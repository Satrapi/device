package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class DeviceControllerImpl implements DeviceController
{
    private final static Logger log = Logger.getLogger(DeviceControllerImpl.class);

    private final Map<Integer,SdwnNodeEntity> nodesMap = new HashMap<>();
    private SdwnNodeEntity sink;

    private DeviceConnectionEntity device;


    @PostConstruct
    public void initBean(){
        this.sink = device.getSinkNode();

        nodesMap.put(sink.getAddress().intValue(),sink);
    }

    @Override
    public void addPacket(PacketEntity packet)
    {

    }

    @Autowired
    public void setDevice(DeviceConnectionEntity device)
    {
        this.device = device;
    }
}
