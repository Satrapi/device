package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.SwitchingNetworkService;
import com.artronics.sdwn.device.SwitchingNetworkServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

@Configuration
public class DeviceHessianConfig
{
    private final static Logger log = Logger.getLogger(DeviceHessianConfig.class);

    @Autowired
    private SwitchingNetworkService switchingNetworkDevice;

    @Bean
    public SwitchingNetworkService getSwitchingNetworkService(){
        return new SwitchingNetworkServiceImpl();
    }

    @Bean(name = "/switchingDevice")
    public HessianServiceExporter pingServiceExport() {
        HessianServiceExporter he = new HessianServiceExporter();
        he.setService(switchingNetworkDevice);
        he.setServiceInterface(SwitchingNetworkService.class);
        return he;
    }
}
