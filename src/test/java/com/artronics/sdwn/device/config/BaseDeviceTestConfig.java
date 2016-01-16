package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.remote.NodeRegistrationService;
import com.artronics.sdwn.device.mocks.MockNodeRegisterationRemoteService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({
        DeviceBaseConfig.class,
})
@PropertySource("classpath:application-defaults-test.properties")
public class BaseDeviceTestConfig
{
    private final static Logger log = Logger.getLogger(BaseDeviceTestConfig.class);
    private NodeRegistrationService nodeRegistrationService;

    @Bean
    public NodeRegistrationService getNodeRegistrationService()
    {
        this.nodeRegistrationService = new MockNodeRegisterationRemoteService();
        return nodeRegistrationService;
    }
}
