package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import com.artronics.sdwn.domain.entities.packet.SdwnReportPacket;
import com.artronics.sdwn.domain.helpers.FakeDevicePacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ControllerTestConfig.class)
public class UnicastSdwnNodeAddressResolverImplTest
{
    protected  static Long fakeId = 0L;

    private Long sinkAddress=0L;

    @Autowired
    private NodeAddressResolver addressResolver;

    @Resource
    @Qualifier("registeredNodes")
    private Set<SdwnNodeEntity> registeredNodes;

    @Autowired
    private DeviceConnectionEntity device;

    private FakeDevicePacketFactory factory = new FakeDevicePacketFactory();

    private SdwnNodeEntity sink;
    private SdwnNodeEntity node30;
    private SdwnNodeEntity node35;
    private SdwnNodeEntity node36;
    private SdwnNodeEntity node100;

    @Before
    public void setUp() throws Exception
    {
        fakeId++;
        sink = new SdwnNodeEntity(sinkAddress);
        sink.setDevice(device);

        node30 = new SdwnNodeEntity(30L);
        node35 = new SdwnNodeEntity(35L);
        node36 = new SdwnNodeEntity(36L);
        node100 = new SdwnNodeEntity(100L);
    }

    @Test
    public void it_should_persist_sink_during_bean_creation(){
        assertTrue(registeredNodes.contains(sink));
    }

    @Test
    public void it_should_register_src_and_dst(){
        SdwnReportPacket packet = factory.createReportPacket(fakeId,node30,node100,node35,node36);
        addressResolver.resolveNodeAddress(packet);

        assertTrue(registeredNodes.contains(node30));
        assertTrue(registeredNodes.contains(node100));
    }
}