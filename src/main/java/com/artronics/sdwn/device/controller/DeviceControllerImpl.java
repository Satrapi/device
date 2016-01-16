package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.controller.services.PacketService;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNeighbor;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import com.artronics.sdwn.domain.entities.packet.PacketFactory;
import com.artronics.sdwn.domain.entities.packet.SdwnReportPacket;
import com.artronics.sdwn.domain.log.PacketLogger;
import com.artronics.sdwn.domain.log.PacketLoggerImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class DeviceControllerImpl implements DeviceController
{
    private final static Logger log = Logger.getLogger(DeviceControllerImpl.class);
    private final static PacketLogger packetLogger= new PacketLoggerImpl(DeviceControllerImpl.class);


    private DeviceConnectionEntity device;

    private NodeAddressResolver addressResolver;

    private NodeRegistrationService nodeRegistrationService;

    private Map<Long,SdwnNodeEntity> registeredNodes;

    private PacketFactory packetFactory;

    private PacketService packetService;

    @Override
    public void addPacket(List<Integer> buff)
    {
        PacketEntity packet = null;
        try {
            packet = (PacketEntity) packetFactory.create(buff);
            packetLogger.log(packet);
            addressResolver.resolveNodeAddress(packet);

            switch (packet.getType()){
                case REPORT:
                    processReportPacket((SdwnReportPacket) packet);
                    break;

                default:
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        try {
            packetService.addPacket(packet);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SdwnReportPacket processReportPacket(SdwnReportPacket packet){
        registerNeighborsIfNotExist(packet);
        return packet;

    }

    private PacketEntity registerNeighborsIfNotExist(SdwnReportPacket packet)
    {
        List<SdwnNeighbor> neighbors = packet.getNeighbors();
        neighbors.forEach(neighbor -> {
            SdwnNodeEntity node = neighbor.getNode();
            if (!registeredNodes.containsKey(node.getAddress())){
                node.setDevice(device);
                node = nodeRegistrationService.registerNode(node);
                registeredNodes.put(node.getAddress(),node);
            }

            neighbor.setNode(registeredNodes.get(node.getAddress()));
        });

        packet.setNeighbors(neighbors);

        return packet;
    }

    @Autowired
    public void setAddressResolver(
            NodeAddressResolver addressResolver)
    {
        this.addressResolver = addressResolver;
    }

    @Resource
    @Qualifier("registeredNodes")
    public void setRegisteredNodes(
            Map<Long, SdwnNodeEntity> registeredNodes)
    {
        this.registeredNodes = registeredNodes;
    }

    @Autowired
    public void setNodeRegistrationService(
            NodeRegistrationService nodeRegistrationService)
    {
        this.nodeRegistrationService = nodeRegistrationService;
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

    @Autowired
    public void setDevice(DeviceConnectionEntity device)
    {
        this.device = device;
    }

}
