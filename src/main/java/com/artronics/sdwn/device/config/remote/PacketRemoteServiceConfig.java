package com.artronics.sdwn.device.config.remote;

import com.artronics.sdwn.controller.services.PacketService;
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
//@ComponentScan(basePackages = "com.artronics.sdwn.device.config.initialize")
@Import(SdwnControllerEntityBeanConfig.class)
public class PacketRemoteServiceConfig extends DeviceBaseConfig
{
    private final static Logger log = Logger.getLogger(PacketRemoteServiceConfig.class);

    private PacketService packetService;

    @Autowired
    private SdwnControllerEntity controllerEntity;

    @PostConstruct
    public void initBean(){
        this.packetService = createPacketService();
    }

    @Bean
    public PacketService getPacketService(){
        return this.packetService;
    }

    public PacketService createPacketService(){
        String serviceUrl = controllerEntity.getUrl()+"/packetService";
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(serviceUrl);
        pb.setServiceInterface(PacketService.class);
        pb.afterPropertiesSet();
        PacketService s = (PacketService) pb.getObject();
        log.debug("Fetching Remote Service: "+s.toString());

        return s;
    }

}
