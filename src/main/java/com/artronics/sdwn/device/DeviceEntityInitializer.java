package com.artronics.sdwn.device;

import com.artronics.sdwn.controller.SdwnController;
import com.artronics.sdwn.device.exception.DeviceConnectionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeviceEntityInitializer implements ApplicationListener<ContextRefreshedEvent>
{
    private final static Logger log = Logger.getLogger(DeviceEntityInitializer.class);

    @Autowired
    SdwnController sdwnController;

    private DeviceDriver serialPort;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        log.debug("Initializing Device Connection");

        serialPort.init();

        try {
            serialPort.open();

        }catch (DeviceConnectionException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setSerialPort(DeviceDriver serialPort)
    {
        this.serialPort = serialPort;
    }
}
