package com.artronics.sdwn.device.mocks;

import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.domain.entities.NetworkSession;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import org.apache.log4j.Logger;

public class MockNodeRegistrationRemoteService implements NodeRegistrationService
{
    private final static Logger log = Logger.getLogger(MockNodeRegistrationRemoteService.class);
    private final static NetworkSession session = new NetworkSession();

    public MockNodeRegistrationRemoteService()
    {
        session.setId(1000L);
    }

    @Override
    public SdwnNodeEntity registerNode(SdwnNodeEntity node)
    {
        node.setId(node.getAddress());
        node.setStatus(SdwnNodeEntity.Status.IDLE);
        node.setSession(session);

        return node;
    }
}
