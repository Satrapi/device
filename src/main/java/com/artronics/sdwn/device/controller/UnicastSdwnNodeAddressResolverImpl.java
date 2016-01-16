package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class UnicastSdwnNodeAddressResolverImpl implements NodeAddressResolver
{
    private final static Logger log = Logger.getLogger(UnicastSdwnNodeAddressResolverImpl.class);

    private Map<Long,SdwnNodeEntity> deviceNodes;

    private SdwnNodeEntity sink;

    private DeviceConnectionEntity device;

    private NodeRegistrationService nodeRegistrationService;

    @PostConstruct
    public void initBean()
    {
        this.sink = device.getSinkNode();

        deviceNodes.put(sink.getAddress(), sink);
    }

    @Override
    public PacketEntity resolveNodeAddress(PacketEntity packet)
    {
        registerSrcAndDstIfNotExist(packet);

        return packet;
    }

    private PacketEntity registerSrcAndDstIfNotExist(PacketEntity packet)
    {
        SdwnNodeEntity srcNode = packet.getSrcNode();
        SdwnNodeEntity dstNode = packet.getDstNode();
        srcNode.setDevice(device);
        dstNode.setDevice(device);

        if (!deviceNodes.containsKey(srcNode.getAddress())) {
            srcNode = nodeRegistrationService.registerNode(packet.getSrcNode());
            deviceNodes.put(srcNode.getAddress(), srcNode);
        }else {
            srcNode = deviceNodes.get(srcNode.getAddress());
        }

        if (!deviceNodes.containsKey(dstNode.getAddress())) {
            dstNode = nodeRegistrationService.registerNode(packet.getDstNode());
            deviceNodes.put(dstNode.getAddress(), dstNode);
        }else {
            dstNode = deviceNodes.get(dstNode.getAddress());
        }

        packet.setSrcNode(srcNode);
        packet.setDstNode(dstNode);

        return packet;
    }


    @Resource
    @Qualifier("deviceNodes")
    public void setDeviceNodes(
            Map<Long, SdwnNodeEntity> deviceNodes)
    {
        this.deviceNodes = deviceNodes;
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
