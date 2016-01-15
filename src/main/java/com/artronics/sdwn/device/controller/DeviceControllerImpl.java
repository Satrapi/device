package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.log.PacketLogger;
import com.artronics.sdwn.controller.services.PacketService;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import com.artronics.sdwn.domain.entities.packet.PacketFactory;
import com.artronics.sdwn.domain.entities.packet.SdwnReportPacket;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceControllerImpl implements DeviceController
{
    private final static Logger log = Logger.getLogger(DeviceControllerImpl.class);

    @Autowired
    private PacketLogger packetLogger;

    private NodeAddressResolver addressResolver;

    private PacketFactory packetFactory;

    private PacketService packetService;

    @Override
    public void addPacket(List<Integer> buff)
    {
        PacketEntity packet =(PacketEntity) packetFactory.create(buff);
        packetLogger.logDevice(packet);
        addressResolver.resolveNodeAddress(packet);

        switch (packet.getType()){
            case REPORT:
                processReportPacket((SdwnReportPacket) packet);
                break;

            default:
        }


        try {
            packetService.addPacket(packet);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SdwnReportPacket processReportPacket(SdwnReportPacket packet){
        return packet;
    }

    @Autowired
    public void setAddressResolver(
            NodeAddressResolver addressResolver)
    {
        this.addressResolver = addressResolver;
    }

    @Autowired
    public void setPacketFactory(PacketFactory packetFactory)
    {
        this.packetFactory = packetFactory;
    }

    @Autowired
    public void setPacketService(PacketService packetService)
    {
        this.packetService = packetService;
    }
}
