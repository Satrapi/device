package com.artronics.sdwn.device;

import com.artronics.sdwn.controller.remote.DeviceRegistrationService;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeviceEntityInitializer implements ApplicationListener<ContextRefreshedEvent>
{
    private final static Logger log = Logger.getLogger(DeviceEntityInitializer.class);

    private String deviceUrl;

    private DeviceRegistrationService registrationService;

    private DeviceConnectionEntity device;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        log.debug("Creating DeviceConnectionEntity with url: " +deviceUrl);

        device = new DeviceConnectionEntity(deviceUrl);
        device = registrationService.register(device);

        log.debug("Device has been registered with associated Controller");
        log.debug(device.toString());
    }

    @Value("${com.artronics.sdwn.device.url}")
    public void setDeviceUrl(String deviceUrl)
    {
        this.deviceUrl = deviceUrl;
    }

    @Autowired
    public void setRegistrationService(
            DeviceRegistrationService registrationService)
    {
        this.registrationService = registrationService;
    }
}
