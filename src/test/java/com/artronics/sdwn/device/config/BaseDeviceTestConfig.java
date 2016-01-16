package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.address.NodeAddressResolver;
import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.device.controller.UnicastSdwnNodeAddressResolverImpl;
import com.artronics.sdwn.device.mocks.MockNodeRegistrationRemoteService;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
@PropertySource("classpath:application-defaults-test.properties")
public class BaseDeviceTestConfig extends DeviceBaseConfig
{
    protected NodeRegistrationService nodeRegistrationService;

    protected DeviceConnectionEntity deviceConnectionEntity;

    protected NodeAddressResolver nodeAddressResolver;

    @Resource
    @Qualifier("registeredNodes")
    protected Set<SdwnNodeEntity> registeredNodes;

    @Bean
    public NodeRegistrationService getNodeRegistrationService()
    {
        this.nodeRegistrationService = new MockNodeRegistrationRemoteService();
        return nodeRegistrationService;
    }

    @Bean
    public NodeAddressResolver getNodeAddressResolver()
    {
        return new UnicastSdwnNodeAddressResolverImpl();
    }

    @Bean
    public DeviceConnectionEntity getDeviceConnectionEntity()
    {
        SdwnNodeEntity sink = new SdwnNodeEntity(sinkAddress);
        sink.setId(sink.getAddress());
        sink.setStatus(SdwnNodeEntity.Status.IDLE);

        this.deviceConnectionEntity = new DeviceConnectionEntity(100L,deviceUrl,sink);
        sink.setDevice(deviceConnectionEntity);

        registeredNodes.add(sink);

        return deviceConnectionEntity;
    }


}
