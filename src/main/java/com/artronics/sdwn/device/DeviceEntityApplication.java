package com.artronics.sdwn.device;

import com.artronics.sdwn.device.config.DeviceEntityConfig;
import com.artronics.sdwn.device.config.SdwnNetworkEntityBeanConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DeviceEntityApplication {

	public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(DeviceEntityApplication.class,
                        DeviceEntityConfig.class,
                        SdwnNetworkEntityBeanConfig.class)
               .build().run(args);
	}
}
