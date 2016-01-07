package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.SdwnController;
import com.artronics.sdwn.device.exception.SdwnControllerNotFound;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.SdwnControllerEntity;
import com.artronics.sdwn.domain.repositories.SdwnControllerRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;

@Configuration
@ComponentScan(basePackages = "com.artronics.sdwn.domain")
public class SdwnNetworkEntityBeanConfig
{
    private final static Logger log = Logger.getLogger(SdwnNetworkEntityBeanConfig.class);

    private SdwnControllerEntity controllerEntity;

    private DeviceConnectionEntity device;

//    @Autowired
    private SdwnController sdwnController;

    @Autowired
    private SdwnControllerRepo controllerRepo;

    private String controllerUrl;

    private String deviceUrl;

    @PostConstruct
    public void initDependencies(){
        this.controllerEntity = createControllerEntity();
        this.sdwnController = createSdwnController();
    }

    @Bean
    public SdwnController getSdwnController(){
        return this.sdwnController;
    }

    @Bean
    public SdwnControllerEntity getControllerEntity(){
        return this.controllerEntity;
    }

    @Bean
    public DeviceConnectionEntity getDevice() throws MalformedURLException
    {
        DeviceConnectionEntity device = new DeviceConnectionEntity(deviceUrl);
        device=sdwnController.registerDeviceConnection(device);
        this.device = device;

        return device;
    }

    public SdwnControllerEntity createControllerEntity() throws SdwnControllerNotFound
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

    public SdwnController createSdwnController(){
        String serviceUrl = controllerEntity.getUrl()+"/sdwnController";
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(serviceUrl);
        pb.setServiceInterface(SdwnController.class);
        pb.afterPropertiesSet();
        SdwnController s = (SdwnController) pb.getObject();

        return s;
    }

    @Value("${com.artronics.sdwn.controller.url}")
    public void setControllerUrl(String controllerUrl)
    {
        this.controllerUrl = controllerUrl;
    }

    @Value("${com.artronics.sdwn.device.url}")
    public void setDeviceUrl(String deviceUrl)
    {
        this.deviceUrl = deviceUrl;
    }

}
