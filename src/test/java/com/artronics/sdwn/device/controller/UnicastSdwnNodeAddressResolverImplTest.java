package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.device.config.BaseDeviceTestConfig;
import com.artronics.sdwn.domain.helpers.FakeDevicePacketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseDeviceTestConfig.class)
public class UnicastSdwnNodeAddressResolverImplTest
{
    @Autowired
    private NodeAddressResolver addressResolver;

    private FakeDevicePacketFactory factory = new FakeDevicePacketFactory();

    @Test
    public void it(){

    }
}