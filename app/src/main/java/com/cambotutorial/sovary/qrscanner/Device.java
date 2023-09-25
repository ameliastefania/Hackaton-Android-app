package com.cambotutorial.sovary.qrscanner;

import java.io.Serializable;

public class Device implements Serializable {
    String MAC;
    String IP_addr;
    String firmware_version;

    String name;

    public Device(String MAC, String IP_addr, String firmware_version, String name) {
        this.MAC = MAC;
        this.IP_addr = IP_addr;
        this.firmware_version = firmware_version;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getIP_addr() {
        return IP_addr;
    }

    public void setIP_addr(String IP_addr) {
        this.IP_addr = IP_addr;
    }

    public String getFirmware_version() {
        return firmware_version;
    }

    public void setFirmware_version(String firmware_version) {
        this.firmware_version = firmware_version;
    }

    @Override
    public String toString() {
        return "Device{" +
                "MAC='" + MAC + '\'' +
                ", IP_addr='" + IP_addr + '\'' +
                ", firmware_version='" + firmware_version + '\'' +
                '}';
    }
}
