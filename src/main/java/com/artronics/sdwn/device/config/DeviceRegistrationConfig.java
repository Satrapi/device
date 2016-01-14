package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.remote.DeviceRegistrationService;
import com.artronics.sdwn.device.config.remote.DeviceRegistrationRemoteServiceConfig;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DeviceRegistrationRemoteServiceConfig.class)
public class DeviceRegistrationConfig extends DeviceBaseConfig
{
    private final static Logger log = Logger.getLogger(DeviceRegistrationConfig.class);

    private DeviceConnectionEntity device;

    @Autowired
    private DeviceRegistrationService deviceRegistrationService;

    @Bean
    public DeviceConnectionEntity getDevice(){
        device = new DeviceConnectionEntity(deviceUrl, sinkAddress);
        device = deviceRegistrationService.register(device);

        log.debug("Device has been registered with associated Controller");
        log.debug(device.toString());

        return device;
    }

}
