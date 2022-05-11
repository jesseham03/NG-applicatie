import javax.swing.*;
import java.awt.Color;

public class MonitoringScroll extends JPanel {



    private static String componentName = "";
    private static String availability = "";
    private static String uptime = "";
    private static String processing = "";
    private static String diskUsage = "";



    MonitoringScroll(Color c ){
        setBackground(c);
    }

    MonitoringScroll(String name, String availability, String uptime, String processing, String diskUsage){
       this.componentName = name;
       this.availability = availability;
       this.uptime = uptime;
       this.processing = processing;
       this.diskUsage = diskUsage;
    }



    public static String getComponentName() {
        return componentName;
    }

    public static String getAvailability() {
        return availability;
    }

    public static String getUptime() {
        return uptime;
    }

    public static String getProcessing() {
        return processing;
    }

    public static String getDiskUsage() {
        return diskUsage;
    }
}
