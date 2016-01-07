package com.artronics.sdwn.device;

import com.artronics.sdwn.controller.SdwnController;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class DeviceEntityInitializer implements ApplicationListener<ContextRefreshedEvent>
{
    private final static Logger log = Logger.getLogger(DeviceEntityInitializer.class);

    @Autowired
    private SdwnController sdwnController;

    private String deviceUrl;

    private DeviceDriver serialPort;

    private DeviceConnectionEntity device;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        log.debug("Initializing Device Connection");

        serialPort.init();

        try {
            registerDevice();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        try {
//            serialPort.open();
//
//        }catch (DeviceConnectionException e) {
//            e.printStackTrace();
//        }
    }

    private void registerDevice() throws MalformedURLException
    {
        device = new DeviceConnectionEntity(deviceUrl);
        device=sdwnController.registerDeviceConnection(device);
    }

    @Autowired
    public void setSerialPort(DeviceDriver serialPort)
    {
        this.serialPort = serialPort;
    }

    @Value("${com.artronics.sdwn.device.url}")
    public void setDeviceUrl(String deviceUrl)
    {
        this.deviceUrl = deviceUrl;
    }
}
