package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.DeviceConnectionService;
import com.artronics.sdwn.device.connection.DeviceConnectionServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.annotation.PostConstruct;

@Configuration
public class DeviceHessianConfig
{
    private final static Logger log = Logger.getLogger(DeviceHessianConfig.class);

    private DeviceConnectionService connectionService;

    @PostConstruct
    public void initBean(){
        this.connectionService = new DeviceConnectionServiceImpl();
    }

    @Bean
    public DeviceConnectionService getSwitchingNetworkService(){
        return this.connectionService;
    }

    @Bean(name = "/deviceService")
    public HessianServiceExporter deviceServiceExport() {
        HessianServiceExporter he = new HessianServiceExporter();
        he.setService(connectionService);
        he.setServiceInterface(DeviceConnectionService.class);
        log.debug("Creating DeviceConnectionService Hessian service: "+he.toString());
        return he;
    }
}
