package com.artronics.sdwn.device.config.remote;

import com.artronics.sdwn.controller.remote.DeviceRegistrationService;
import com.artronics.sdwn.device.config.DeviceBaseConfig;
import com.artronics.sdwn.device.config.initialize.SdwnControllerEntityBeanConfig;
import com.artronics.sdwn.domain.entities.SdwnControllerEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import javax.annotation.PostConstruct;

@Configuration
@Import(SdwnControllerEntityBeanConfig.class)
public class DeviceRegistrationRemoteServiceConfig extends DeviceBaseConfig
{
    private final static Logger log = Logger.getLogger(DeviceRegistrationRemoteServiceConfig.class);

    private DeviceRegistrationService deviceRegistrationService;

    @Autowired
    private SdwnControllerEntity controllerEntity;

    @PostConstruct
    public void initBean(){
        this.deviceRegistrationService = createRegistrationService();
    }

    @Bean
    public DeviceRegistrationService getDeviceRegistrationService()
    {
        return deviceRegistrationService;
    }

    public DeviceRegistrationService createRegistrationService(){
        String serviceUrl = controllerEntity.getUrl()+"/registerDevice";
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(serviceUrl);
        pb.setServiceInterface(DeviceRegistrationService.class);
        pb.afterPropertiesSet();
        DeviceRegistrationService s = (DeviceRegistrationService) pb.getObject();
        log.debug("Fetching Remote Service: "+s.toString());

        return s;
    }
}
