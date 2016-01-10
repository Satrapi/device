package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.SdwnController;
import com.artronics.sdwn.controller.exceptions.SdwnControllerNotFound;
import com.artronics.sdwn.controller.remote.DeviceRegistrationService;
import com.artronics.sdwn.controller.services.PacketService;
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

@Configuration
@ComponentScan(basePackages = "com.artronics.sdwn.domain")
public class SdwnNetworkEntityBeanConfig
{
    private final static Logger log = Logger.getLogger(SdwnNetworkEntityBeanConfig.class);

    private SdwnControllerEntity controllerEntity;

    private DeviceRegistrationService registrationService;

    private PacketService packetService;

    private DeviceConnectionEntity device;

    private SdwnController sdwnController;

    @Autowired
    private SdwnControllerRepo controllerRepo;

    private String controllerUrl;

    private String deviceUrl;

    private Long sinkAddress;

    @PostConstruct
    public void initDependencies(){
        this.controllerEntity = createControllerEntity();
        this.registrationService = createRegistrationService();
        this.packetService = createPacketService();
//        this.sdwnController = createSdwnController();
    }

    @Bean
    public DeviceRegistrationService getRegistrationService(){
        return this.registrationService;
    }

    @Bean
    public PacketService getPacketService(){
        return this.packetService;
    }

    @Bean
    public DeviceConnectionEntity getDevice(){
        device = new DeviceConnectionEntity(deviceUrl,sinkAddress);
        device = registrationService.register(device);

        log.debug("Device has been registered with associated Controller");
        log.debug(device.toString());

        return device;
    }

    @Bean
    public SdwnControllerEntity getControllerEntity(){
        return this.controllerEntity;
    }

    public SdwnControllerEntity createControllerEntity() throws SdwnControllerNotFound
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

    public SdwnController createSdwnController(){
        String serviceUrl = controllerEntity.getUrl()+"/sdwnController";
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(serviceUrl);
        pb.setServiceInterface(SdwnController.class);
        pb.afterPropertiesSet();
        SdwnController s = (SdwnController) pb.getObject();
        log.debug("Fetching Remote Service: "+s.toString());

        this.sdwnController = s;
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

    @Value("${com.artronics.sdwn.device.sink_address}")
    public void setSinkAddress(Long sinkAddress)
    {
        this.sinkAddress = sinkAddress;
    }

}
