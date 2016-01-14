package com.artronics.sdwn.device.controller;

import com.artronics.sdwn.domain.entities.packet.PacketEntity;

public interface DeviceController
{
    void addPacket(PacketEntity packet);
}
