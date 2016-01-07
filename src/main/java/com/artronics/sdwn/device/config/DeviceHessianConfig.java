package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.DeviceConnectionService;
import com.artronics.sdwn.device.DeviceConnectionServiceImpl;
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
    private DeviceConnectionService switchingNetworkDevice;

    @Bean
    public DeviceConnectionService getSwitchingNetworkService(){
        return new DeviceConnectionServiceImpl();
    }

    @Bean(name = "/switchingDevice")
    public HessianServiceExporter pingServiceExport() {
        HessianServiceExporter he = new HessianServiceExporter();
        he.setService(switchingNetworkDevice);
        he.setServiceInterface(DeviceConnectionService.class);
        return he;
    }
}
