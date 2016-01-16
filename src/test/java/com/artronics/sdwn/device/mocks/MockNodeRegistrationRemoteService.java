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
        //Create a new instance so we can simulate db and deserialization
        SdwnNodeEntity persistedNode = new SdwnNodeEntity();

        persistedNode.setAddress(node.getAddress());
        persistedNode.setId(node.getAddress());
        persistedNode.setStatus(SdwnNodeEntity.Status.IDLE);
        persistedNode.setSession(session);

        return node;
    }
}
