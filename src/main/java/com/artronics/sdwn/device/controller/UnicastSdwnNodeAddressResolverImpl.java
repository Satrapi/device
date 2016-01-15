package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNeighbor;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import com.artronics.sdwn.domain.entities.packet.Packet;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class UnicastSdwnNodeAddressResolverImpl implements NodeAddressResolver
{
    private final static Logger log = Logger.getLogger(UnicastSdwnNodeAddressResolverImpl.class);

    private final Map<Long, SdwnNodeEntity> nodesMap = new HashMap<>();

    private SdwnNodeEntity sink;

    private DeviceConnectionEntity device;

    private NodeRegistrationService nodeRegistrationService;

    @PostConstruct
    public void initBean()
    {
        this.sink = device.getSinkNode();

        nodesMap.put(sink.getAddress(), sink);
    }

    @Override
    public PacketEntity resolveNodeAddress(PacketEntity packet)
    {
        registerSrcAndDstIfNotExist(packet);

        if (packet.getType().equals(Packet.Type.REPORT)) {
            registerNeighborsIfNotExist(packet);
        }

        return packet;
    }

    private PacketEntity registerNeighborsIfNotExist(PacketEntity packet)
    {
        Set<SdwnNeighbor> neighbors = SdwnNeighbor.createNeighbors(packet);
        neighbors.forEach(neighbor -> {
            SdwnNodeEntity node = neighbor.getNode();
            if (!nodesMap.containsKey(node.getAddress())){
                node.setDevice(device);
                node = nodeRegistrationService.registerNode(node);
                nodesMap.put(node.getAddress(),node);
            }
        });

        return packet;
    }

    private PacketEntity registerSrcAndDstIfNotExist(PacketEntity packet)
    {
        SdwnNodeEntity srcNode = packet.getSrcNode();
        SdwnNodeEntity dstNode = packet.getDstNode();
        srcNode.setDevice(device);
        dstNode.setDevice(device);

        if (!nodesMap.containsKey(srcNode.getAddress())) {
            srcNode = nodeRegistrationService.registerNode(packet.getSrcNode());
            nodesMap.put(srcNode.getAddress(), srcNode);
        }else {
            srcNode = nodesMap.get(srcNode.getAddress());
        }

        if (!nodesMap.containsKey(dstNode.getAddress())) {
            dstNode = nodeRegistrationService.registerNode(packet.getDstNode());
            nodesMap.put(dstNode.getAddress(), dstNode);
        }else {
            dstNode = nodesMap.get(dstNode.getAddress());
        }

        packet.setSrcNode(srcNode);
        packet.setDstNode(dstNode);


        return packet;
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
