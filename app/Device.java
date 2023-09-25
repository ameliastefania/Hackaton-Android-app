public class Device {
    String MAC;
    String IP_addr;
    String firmware_current_version;

    public Device(String MAC, String IP_addr, String firmware_current_version) {
        this.MAC = MAC;
        this.IP_addr = IP_addr;
        this.firmware_current_version = firmware_current_version;
    }

    public String getMAC() {
        return MAC;
    }

    public String getIP_addr() {
        return IP_addr;
    }

    public String getFirmware_current_version() {
        return firmware_current_version;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public void setIP_addr(String IP_addr) {
        this.IP_addr = IP_addr;
    }

    public void setFirmware_current_version(String firmware_current_version) {
        this.firmware_current_version = firmware_current_version;
    }

    @Override
    public String toString() {
        return "Device{" +
                "MAC='" + MAC + '\'' +
                ", IP_addr='" + IP_addr + '\'' +
                ", firmware_current_version='" + firmware_current_version + '\'' +
                '}';
    }
}
