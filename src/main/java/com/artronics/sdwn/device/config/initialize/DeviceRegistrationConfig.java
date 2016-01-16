package com.artronics.sdwn.device.config.initialize;

import com.artronics.sdwn.controller.remote.DeviceRegistrationService;
import com.artronics.sdwn.device.config.DeviceBaseConfig;
import com.artronics.sdwn.device.config.remote.DeviceRegistrationRemoteServiceConfig;
import com.artronics.sdwn.device.config.remote.NodeRegistrationRemoteServiceConfig;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.node.SdwnNodeEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@Import({
        DeviceRegistrationRemoteServiceConfig.class,
        NodeRegistrationRemoteServiceConfig.class
})
public class DeviceRegistrationConfig extends DeviceBaseConfig
{
    private final static Logger log = Logger.getLogger(DeviceRegistrationConfig.class);

    private DeviceConnectionEntity device;

    private Map<Long,SdwnNodeEntity> nodesMap;

    @Autowired
    private DeviceRegistrationService deviceRegistrationService;

    @Bean
    public DeviceConnectionEntity getDevice(){
        SdwnNodeEntity sink = SdwnNodeEntity.create(sinkAddress);
        sink.setType(SdwnNodeEntity.Type.SINK);

        device = new DeviceConnectionEntity(deviceUrl);
        device = deviceRegistrationService.registerDevice(device, sink);

        log.debug("Device has been registered with associated Controller");
        log.debug(device.toString());

        log.debug("Add sink to registeredNodes");
        nodesMap.put(sinkAddress,sink);

        return device;
    }

    @Resource
    @Qualifier("registeredNodes")
    public void setNodesMap(
            Map<Long, SdwnNodeEntity> nodesMap)
    {
        this.nodesMap = nodesMap;
    }
}
