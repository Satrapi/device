package com.artronics.sdwn.device.mocks;

import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import org.apache.log4j.Logger;

public class MockNodeRegisterationRemoteService implements NodeRegistrationService
{
    private final static Logger log = Logger.getLogger(MockNodeRegisterationRemoteService.class);

    @Override
    public SdwnNodeEntity registerNode(SdwnNodeEntity node)
    {
        node.setId(node.getAddress());
        node.setStatus(SdwnNodeEntity.Status.IDLE);

        return node;
    }
}
