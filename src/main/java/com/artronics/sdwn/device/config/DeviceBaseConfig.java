package com.artronics.sdwn.device.config;

import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan(basePackages = "com.artronics.sdwn.controller.log")
public class DeviceBaseConfig
{
    private final static Logger log = Logger.getLogger(DeviceBaseConfig.class);

    protected Set<SdwnNodeEntity> registeredNodes;

    protected String controllerUrl;

    protected String deviceUrl;

    protected Long sinkAddress;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "registeredNodes")
    public Set<SdwnNodeEntity> getRegisteredNodes()
    {
        return new HashSet<>();
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
