package com.artronics.sdwn.device.config.server.hessian;

import com.artronics.sdwn.controller.DeviceConnectionService;
import com.artronics.sdwn.device.connection.DeviceConnectionServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.annotation.PostConstruct;

@Configuration
public class DeviceConnectionHessianServiceConfig
{
    private final static Logger log = Logger.getLogger(DeviceConnectionHessianServiceConfig.class);

    private DeviceConnectionService deviceConnectionService;

    @PostConstruct
    public void initBean(){
        this.deviceConnectionService = new DeviceConnectionServiceImpl();
    }

    @Bean
    public DeviceConnectionService getSwitchingNetworkService(){
        return this.deviceConnectionService;
    }

    @Bean(name = "/deviceService")
    public HessianServiceExporter deviceServiceExport() {
        HessianServiceExporter he = new HessianServiceExporter();
        he.setService(deviceConnectionService);
        he.setServiceInterface(DeviceConnectionService.class);
        log.debug("Creating DeviceConnectionService Hessian service: "+he.toString());
        return he;
    }


}
