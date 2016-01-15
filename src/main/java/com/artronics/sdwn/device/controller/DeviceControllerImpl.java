package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.services.PacketService;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceControllerImpl implements DeviceController
{
    private final static Logger log = Logger.getLogger(DeviceControllerImpl.class);

    private NodeAddressResolver addressResolver;

    private PacketService packetService;

    @Override
    public void addPacket(PacketEntity packet)
    {
        packet = addressResolver.resolveNodeAddress(packet);

        try {
            packetService.addPacket(packet);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setAddressResolver(
            NodeAddressResolver addressResolver)
    {
        this.addressResolver = addressResolver;
    }

    @Autowired
    public void setPacketService(PacketService packetService)
    {
        this.packetService = packetService;
    }
}
