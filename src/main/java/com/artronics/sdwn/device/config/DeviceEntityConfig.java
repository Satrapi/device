package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.SdwnController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

@Configuration
@ComponentScan(basePackages = "com.artronics.sdwn.device")
@PropertySource("classpath:application.properties")
public class DeviceEntityConfig
{
    private final static Logger log = Logger.getLogger(DeviceEntityConfig.class);

    private String controllerUrl;

    @Bean
    SdwnController getSdwnController(){
        HessianProxyFactoryBean pb = new HessianProxyFactoryBean();
        pb.setServiceUrl(controllerUrl);
        pb.setServiceInterface(SdwnController.class);
        pb.afterPropertiesSet();
        SdwnController s = (SdwnController) pb.getObject();

        return s;
    }

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
