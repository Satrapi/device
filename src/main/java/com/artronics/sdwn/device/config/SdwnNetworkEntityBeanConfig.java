package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.SdwnController;
import com.artronics.sdwn.device.exception.SdwnControllerNotFound;
import com.artronics.sdwn.domain.config.SdwnDomainConfig;
import com.artronics.sdwn.domain.entities.SdwnControllerEntity;
import com.artronics.sdwn.domain.entities.SwitchingNetwork;
import com.artronics.sdwn.domain.repositories.SdwnControllerRepo;
import com.artronics.sdwn.domain.repositories.SwitchingNetRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

@Configuration
@Import(SdwnDomainConfig.class)
public class SdwnNetworkEntityBeanConfig
{
    private final static Logger log = Logger.getLogger(SdwnNetworkEntityBeanConfig.class);

    private SdwnControllerEntity controllerEntity;
    private SwitchingNetwork device;

    @Autowired
    private SdwnControllerRepo controllerRepo;

    @Autowired
    private SwitchingNetRepo netRepo;

    @Value("${com.artronics.sdwn.controller.url}")
    private String controllerUrl;

    @Value("${com.artronics.sdwn.device.url}")
    private String deviceUrl;

    @Bean(name = "sdwnControllerEntity")
    public SdwnControllerEntity getController() throws SdwnControllerNotFound
    {
        controllerEntity=controllerRepo.findByUrl(controllerUrl);

        if (controllerEntity == null){
            throw new SdwnControllerNotFound("There is no SdwnController with url" +controllerUrl);
        }

        if (controllerEntity.getStatus()!= SdwnControllerEntity.Status.ACTIVE) {
            throw new SdwnControllerNotFound("The Controller is no longer ACTIVE. Make sure you run the controller first");
        }

        return controllerEntity;
    }

    @Bean
    @DependsOn("sdwnControllerEntity")
    SdwnController getSdwnController(){
        String serviceUrl = controllerEntity.getUrl()+"/sdwnController";
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(serviceUrl);
        pb.setServiceInterface(SdwnController.class);
        pb.afterPropertiesSet();
        SdwnController s = (SdwnController) pb.getObject();

        return s;
    }
}
