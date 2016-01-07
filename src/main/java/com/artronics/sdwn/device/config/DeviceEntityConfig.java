package com.artronics.sdwn.device.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = "com.artronics.sdwn.device")
@PropertySource("classpath:application.properties")
public class DeviceEntityConfig
{
final static Logger log = Logger.getLogger(DeviceEntityConfig.class);

    private String controllerUrl;


    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${com.artronics.sdwn.controller.url}")
    public void setControllerUrl(String controllerUrl)
    {
        this.controllerUrl = controllerUrl;
    }
}
