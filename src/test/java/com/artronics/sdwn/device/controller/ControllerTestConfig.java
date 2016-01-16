package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.device.config.BaseDeviceTestConfig;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerTestConfig extends BaseDeviceTestConfig
{
    private final static Logger log = Logger.getLogger(ControllerTestConfig.class);
    private DeviceConnectionEntity deviceConnectionEntity;

    @Override
    @Bean
    public DeviceConnectionEntity getDeviceConnectionEntity()
    {
        SdwnNodeEntity sink = new SdwnNodeEntity(sinkAddress);
        sink.setId(sink.getAddress());
        sink.setStatus(SdwnNodeEntity.Status.IDLE);

        this.deviceConnectionEntity = new DeviceConnectionEntity(100L,deviceUrl,sink);
        sink.setDevice(deviceConnectionEntity);

        return deviceConnectionEntity;
    }
}
