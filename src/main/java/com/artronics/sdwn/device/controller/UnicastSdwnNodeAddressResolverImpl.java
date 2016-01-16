package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNeighbor;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import com.artronics.sdwn.domain.entities.packet.Packet;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import com.artronics.sdwn.domain.entities.packet.SdwnReportPacket;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
public class UnicastSdwnNodeAddressResolverImpl implements NodeAddressResolver
{
    private final static Logger log = Logger.getLogger(UnicastSdwnNodeAddressResolverImpl.class);

    private Set<SdwnNodeEntity> registeredNodes;

    private SdwnNodeEntity sink;

    private DeviceConnectionEntity device;

    private NodeRegistrationService nodeRegistrationService;

    @PostConstruct
    public void initBean()
    {
        this.sink = device.getSinkNode();

        registeredNodes.add(sink);
    }

    @Override
    public PacketEntity resolveNodeAddress(PacketEntity packet)
    {
        registerSrcAndDstIfNotExist(packet);

        if (packet.getType().equals(Packet.Type.REPORT)) {
            registerNeighborsIfNotExist((SdwnReportPacket) packet);
        }

        return packet;
    }

    private PacketEntity registerSrcAndDstIfNotExist(PacketEntity packet)
    {
        SdwnNodeEntity srcNode = packet.getSrcNode();
        SdwnNodeEntity dstNode = packet.getDstNode();
        srcNode.setDevice(device);
        dstNode.setDevice(device);

        if (!registeredNodes.contains(srcNode)) {
            nodeRegistrationService.registerNode(packet.getSrcNode());
            registeredNodes.add(packet.getSrcNode());
        }else {

        }

        if (!registeredNodes.contains(dstNode)) {
            nodeRegistrationService.registerNode(packet.getDstNode());
            registeredNodes.add(packet.getDstNode());
        }else {
        }

        return packet;
    }

    private PacketEntity registerNeighborsIfNotExist(SdwnReportPacket packet)
    {
        List<SdwnNeighbor> neighbors = packet.getNeighbors();
        neighbors.forEach(neighbor -> {
            SdwnNodeEntity node = neighbor.getNode();
            if (!registeredNodes.contains(node)){
                node.setDevice(device);
                node = nodeRegistrationService.registerNode(node);
                registeredNodes.add(node);
            }

            neighbor.setNode(node);
        });

        packet.setNeighbors(neighbors);

        return packet;
    }


    @Resource
    @Qualifier("registeredNodes")
    public void setRegisteredNodes(
            Set<SdwnNodeEntity> registeredNodes)
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
    public void setDevice(DeviceConnectionEntity device)
    {
        this.device = device;
    }

}
