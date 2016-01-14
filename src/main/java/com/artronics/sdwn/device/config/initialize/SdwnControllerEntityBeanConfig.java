package com.artronics.sdwn.device.config.initialize;

import com.artronics.sdwn.controller.exceptions.SdwnControllerNotFound;
import com.artronics.sdwn.device.config.DeviceBaseConfig;
import com.artronics.sdwn.domain.entities.SdwnControllerEntity;
import com.artronics.sdwn.domain.repositories.SdwnControllerRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.artronics.sdwn.domain")
public class SdwnControllerEntityBeanConfig extends DeviceBaseConfig
{
    private final static Logger log = Logger.getLogger(SdwnControllerEntityBeanConfig.class);

    private SdwnControllerEntity controllerEntity;

    @Autowired
    private SdwnControllerRepo controllerRepo;

    @PostConstruct
    public void initBean(){
        this.controllerEntity = createControllerEntity();
    }

    @Bean
    public SdwnControllerEntity getControllerEntity()
    {
        return controllerEntity;
    }

    private SdwnControllerEntity createControllerEntity() throws SdwnControllerNotFound
    {
        log.debug("Retrieving SdwnControllerEntity based on config URL: " +controllerUrl);
        controllerEntity=controllerRepo.findByUrl(controllerUrl);

        if (controllerEntity == null){
            throw new SdwnControllerNotFound("There is no SdwnController with url" +controllerUrl);
        }

        if (controllerEntity.getStatus()!= SdwnControllerEntity.Status.ACTIVE) {
            throw new SdwnControllerNotFound("The Controller is no longer ACTIVE. Make sure you run the controller first");
        }

        return controllerEntity;
    }
}
