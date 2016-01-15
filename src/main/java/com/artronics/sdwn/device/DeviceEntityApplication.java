package com.artronics.sdwn.device;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "com.artronics.sdwn.device")
@PropertySource("classpath:application.properties")
//@Import(SdwnDomainApplication.class)
public class DeviceEntityApplication {

	public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(DeviceEntityApplication.class
        )
               .build().run(args);
	}

}
