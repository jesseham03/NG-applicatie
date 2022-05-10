import javax.swing.*;
import java.awt.Color;

public class MonitoringScroll extends JPanel {


    private String availability;

    private String uptime;

    private String processing;

    private String diskUsage;



    MonitoringScroll(Color c ){
        setBackground(c);
    }

    MonitoringScroll(String availability, String uptime, String processing, String diskUsage){
        availability = this.availability;
        uptime = this.uptime;
        processing = this.processing;
        diskUsage = this.diskUsage;
    }




    public String getAvailability() {
        return availability;
    }

    public String getUptime() {
        return uptime;
    }

    public String getProcessing() {
        return processing;
    }

    public String getDiskUsage() {
        return diskUsage;
    }
}
