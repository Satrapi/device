package com.artronics.sdwn.device.serialPort;

import com.artronics.sdwn.device.DeviceDriver;
import com.artronics.sdwn.device.buffer.BufferDistributor;
import com.artronics.sdwn.device.exception.DeviceConnectionException;
import gnu.io.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

@Component
public class DeviceDriverSerialPort implements DeviceDriver,SerialPortEventListener
{
    private final static Logger log = Logger.getLogger(DeviceDriverSerialPort.class);

    private BufferDistributor bufferDistributor;

    private String connectionString;
    private Integer timeout;

    private InputStream input;
    private OutputStream output;
    private CommPortIdentifier identifier;
    private SerialPort serialPort;

    @Override
    public void init()
    {
        log.debug("Initializing Device Driver...");
        log.debug("Connection String is: \""+connectionString+"\"");
        setConnection();
    }

    private void setConnection()
    {
        Enumeration portsEnum = CommPortIdentifier.getPortIdentifiers();

        log.debug("Searching for available serial ports:");
        int count=0;
        while (portsEnum.hasMoreElements()) {
            count++;
            CommPortIdentifier port = (CommPortIdentifier) portsEnum.nextElement();

            log.debug(count + " : \""+port.getName()+"\"");
            if (port.getName().equals(connectionString)) {
                identifier = port;

                log.debug("Match found.");
                return;
            }
        }
        log.debug("No match found. Remember Connection String must be equal to com port's name");
    }
    @Override
    public void open() throws DeviceConnectionException
    {
        log.debug("Opening Serial port...");

        if (identifier == null) {
            throw new DeviceConnectionException("Attempt to open a null connection.");
        }

        final CommPort commPort;
        SerialPort serialPort = null;

        try {
            commPort = identifier.open("SinkPort", timeout);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort) commPort;

            serialPort.setSerialPortParams(115200,
                                           SerialPort.DATABITS_8,
                                           SerialPort.STOPBITS_1,
                                           SerialPort.PARITY_NONE);


        }catch (Exception e) {
            e.printStackTrace();
            throw new DeviceConnectionException("Can not open the connection");
        }

        if (serialPort != null) {
            this.serialPort = serialPort;
            initEventListenersAndIO();
            addInputToBuffDistributor();

        }else
            throw new DeviceConnectionException("No serial port has found. serialPort is null");
    }

    private void addInputToBuffDistributor()
    {
        bufferDistributor.setInput(input);
    }

    private void initEventListenersAndIO() throws DeviceConnectionException
    {
        try {
            log.debug("Initializing Event Listener, Input and Output Streams.");
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        }catch (TooManyListenersException e) {
            e.printStackTrace();
            log.error("Too many listener");
            throw new DeviceConnectionException("Too many listener");
        }catch (IOException e) {
            e.printStackTrace();
            throw new DeviceConnectionException("IO exception while opening streams.");
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent)
    {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            bufferDistributor.bufferReceived();
        }
    }

    @Autowired
    public void setBufferDistributor(
            BufferDistributor bufferDistributor)
    {
        this.bufferDistributor = bufferDistributor;
    }

    @Value("${com.artronics.sdwn.device.connection.connection_string}")
    public void setConnectionString(String connectionString)
    {
        this.connectionString = connectionString;
    }

    @Value("${com.artronics.sdwn.device.connection.timeout}")
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }
}
