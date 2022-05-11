import javax.management.monitor.Monitor;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Toolkit.getDefaultToolkit;

public class Frame extends JFrame implements ActionListener {
    private final Network network = new Network();

    //region JUIcomponents
    private final JButton addNewComponentButton;
    private JButton RefreshButton;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField availabilityField;

    private final JLabel totalPriceLabelValue;
    private final JLabel totalAvailabilityLabelValue;

    private JTextArea componentList;

    private final JMenuItem openMonitoringButton;
    private final JMenuItem openOptimisationButton;
    private final JMenuItem openHomeButton;
    private final JMenuItem openFileButton;
    private final JMenuItem saveFileButton;
    private final JMenuItem quitButton;

    private final JMenu openButton;

    private final JComboBox<InfrastructureComponent.Type> typeComboBox;

//    private DragDropPanel visualNetwork;

    private final JPanel netWorkDrawing;
    private final JPanel bottomPanel;
    private  JPanel monitoring;
    private JPanel optimisation;

    private JScrollPane scrollpane;

    public static Map<String, ImageIcon> imageMap = null;

    //endregion

    public Frame() {
        try {
//            setUndecorated(true);
            com.formdev.flatlaf.FlatDarculaLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setIconImage(getDefaultToolkit().getImage(getClass().getResource("/Favicon2.png")));
        setTitle("NG Network-Application");
        setSize(650, 450);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUIFont (new javax.swing.plaf.FontUIResource("Roboto",Font.PLAIN,15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //region Menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu m1 = new JMenu("Start");
        openButton = new JMenu("Open");
        menuBar.add(m1);
        menuBar.add(openButton);
        openOptimisationButton = new JMenuItem("Optimisation");
        openMonitoringButton = new JMenuItem("Monitoring");
        openHomeButton = new JMenuItem("Home");
        openFileButton = new JMenuItem("Open");
        saveFileButton = new JMenuItem("Save as");
        quitButton = new JMenuItem("Quit");
        openHomeButton.addActionListener(this);
        openOptimisationButton.addActionListener(this);
        openMonitoringButton.addActionListener(this);
        openFileButton.addActionListener(this);
        saveFileButton.addActionListener(this);
        quitButton.addActionListener(this);
        m1.add(openFileButton);
        m1.add(saveFileButton);
        m1.add(quitButton);
        openButton.add(openHomeButton);
        openButton.add(openMonitoringButton);
        openButton.add(openOptimisationButton);

        //endregion

        //region Bottombar
        bottomPanel = new JPanel();

        //region AddNewComponent section
        JPanel newComponentPanel = new JPanel();
        JLabel newComponentLabel = new JLabel("New Component");
        nameField = new JTextField(5);
        JLabel priceLabel = new JLabel("Price");
        priceField = new JTextField(5);
        JLabel availabilityLabel = new JLabel("Availability");
        availabilityField = new JTextField(5);
        typeComboBox = new JComboBox<>(InfrastructureComponent.Type.values());
        addNewComponentButton = new JButton("Add");
        addNewComponentButton.addActionListener(this);
        newComponentPanel.add(newComponentLabel);
        newComponentPanel.add(nameField);
        newComponentPanel.add(priceLabel);
        newComponentPanel.add(priceField);
        newComponentPanel.add(availabilityLabel);
        newComponentPanel.add(availabilityField);
        newComponentPanel.add(typeComboBox);
        newComponentPanel.add(addNewComponentButton);
        //endregion

        //region Statistics section
        JPanel statsPanel = new JPanel();
        JLabel totalPriceLabel = new JLabel("Price:");
        totalPriceLabelValue = new JLabel("0");
        JLabel totalAvailabilityLabel = new JLabel("Availability:");
        totalAvailabilityLabelValue = new JLabel("0");
        statsPanel.add(totalPriceLabel);
        statsPanel.add(totalPriceLabelValue);
        statsPanel.add(totalAvailabilityLabel);
        statsPanel.add(totalAvailabilityLabelValue);
        //endregion

        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.add(statsPanel);
        bottomPanel.add(newComponentPanel);
        //endregion

        netWorkDrawing = new JPanel();
        netWorkDrawing.setLayout(null);

        //For visual appearance
//        visualNetwork = new DragDropPanel(network);

        //For text appearance
//        componentList = new JTextArea();
//        componentList.setEditable(false);

        getContentPane().add(BorderLayout.CENTER, netWorkDrawing);
        getContentPane().add(BorderLayout.SOUTH, bottomPanel);
        getContentPane().add(BorderLayout.NORTH, menuBar);
        //For visual appearance
//        getContentPane().add(BorderLayout.CENTER, visualNetwork);
        //For text appearance
//        getContentPane().add(BorderLayout.CENTER, componentList);

//        FrameDragListener frameDragListener = new FrameDragListener(this);
//        addMouseListener(frameDragListener);
//        addMouseMotionListener(frameDragListener);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addNewComponentButton) {
            CreateComponent();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        } else if (e.getSource() == openFileButton) {
            OpenFile();
        } else if (e.getSource() == saveFileButton) {
            saveToFile();
        } else if(e.getSource() == openMonitoringButton){
            OpenMonitoring();
        } else if(e.getSource() == openOptimisationButton) {
            OpenOptimisation();
        } else if(e.getSource() == openHomeButton){
            OpenHome();
        } else if(e.getSource() == RefreshButton){
            Refresh();
        }
    }


    private void OpenMonitoring(){
        try{
            monitoring = new JPanel();
            monitoring.setLayout(new FlowLayout());

            JPanel MonitoringInfo = new JPanel();
            MonitoringInfo.setLayout(new GridLayout(6,1));
            RefreshButton = new JButton("Refresh");
            JLabel InfoName = new JLabel("Server Name: " + MonitoringScroll.getComponentName());
            JLabel InfoAvailability = new JLabel("Availability: "  + MonitoringScroll.getAvailability());
            JLabel InfoTimeAvailabality = new JLabel("Uptime: " + MonitoringScroll.getUptime());
            JLabel InfoProcessing = new JLabel("Processing Power Used: " + MonitoringScroll.getProcessing());
            JLabel InfoDisk = new JLabel("Disk Usage: "+  MonitoringScroll.getDiskUsage());
            MonitoringInfo.add(InfoName);
            MonitoringInfo.add(InfoAvailability);
            MonitoringInfo.add(InfoTimeAvailabality);
            MonitoringInfo.add(InfoProcessing);
            MonitoringInfo.add(InfoDisk);
            MonitoringInfo.add(RefreshButton);
            RefreshButton.addActionListener(this);

            RefreshButton.setFont(new Font("Robota", Font.PLAIN, 30));
            InfoName.setFont(new Font("Robota", Font.PLAIN, 30));
            InfoAvailability.setFont(new Font("Robota", Font.PLAIN, 30));
            InfoDisk.setFont(new Font("Robota", Font.PLAIN, 30));
            InfoProcessing.setFont(new Font("Robota", Font.PLAIN, 30));
            InfoTimeAvailabality.setFont(new Font("Robota", Font.PLAIN, 30));


            String categories[] = {"Database Server 1", "Database Server 2", "Webserver 1", "Webserver 2", "Firewall" };
            imageMap = createImageMap(categories);



            JList list = new JList(categories);
            list.setCellRenderer(new ListRenderer());
            scrollpane = new JScrollPane(list);


            setVisible(true);
            //monitoring.add(MonitoringBar);
            monitoring.add(MonitoringInfo);
            getContentPane().add(scrollpane, BorderLayout.WEST);
            getContentPane().add(monitoring, BorderLayout.CENTER);
            bottomPanel.setVisible(false);
            netWorkDrawing.setVisible(false);
            optimisation.setVisible(false);



            setVisible(true);

        } catch(Exception e){
            return;
        }
    }

    private Map<String, ImageIcon> createImageMap(String[] list) {
        Map<String, ImageIcon> map = new HashMap<>();
        try {
            map.put("Database Server 1", new ImageIcon(getDefaultToolkit().getImage(getClass().getResource("/databaseservericon.png"))));
            map.put("Database Server 2", new ImageIcon(getDefaultToolkit().getImage(getClass().getResource("/databaseservericon.png"))));
            map.put("Webserver 1", new ImageIcon(getDefaultToolkit().getImage(getClass().getResource("/webservericon.png"))));
            map.put("Webserver 2", new ImageIcon(getDefaultToolkit().getImage(getClass().getResource("/webservericon.png"))));
            map.put("Firewall", new ImageIcon(getDefaultToolkit().getImage(getClass().getResource("/firewallicon.png"))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }


    private void OpenOptimisation(){
        try{
            optimisation = new JPanel();
            optimisation.setLayout(new FlowLayout());
            setVisible(true);

            scrollpane.setVisible(false);
            bottomPanel.setVisible(false);
            netWorkDrawing.setVisible(false);
            monitoring.setVisible(false);
        } catch(Exception e){
            return;
        }
    }

    private void Refresh(){
        try{
            String[] command =
                    {
                            "cmd",
                    };
            Process p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println("ssh 145.44.233.80");
            stdin.close();
            int returnCode = p.waitFor();
            System.out.println("Return code = " + returnCode);
        }catch(Exception e){
            return;
        }
    }



    private void OpenHome(){
        try{
            bottomPanel.setVisible(true);
            netWorkDrawing.setVisible(true);
            monitoring.setVisible(false);
            optimisation.setVisible(false);
        }catch(Exception e){
            return;
        }
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }


    private void CreateComponent() {
        try {
            float price = Float.parseFloat(priceField.getText());
            float availability = Float.parseFloat(availabilityField.getText());
            InfrastructureComponent.Type selectedType = (InfrastructureComponent.Type) typeComboBox.getSelectedItem();
            InfrastructureComponent addedComponent = new InfrastructureComponent(nameField.getText(), price, availability, selectedType);
            network.addComponent(addedComponent);

            DraggableImageComponent visualComponent = new DraggableImageComponent(addedComponent);
            netWorkDrawing.add(visualComponent);
            visualComponent.setOverbearing(true);

            int size = 50;
            int centerX = netWorkDrawing.getWidth() / 2;
            int centerY = netWorkDrawing.getHeight() / 2;
            visualComponent.setSize(size, size);
            visualComponent.setLocation(centerX - visualComponent.getWidth() / 2, centerY - visualComponent.getHeight() / 2);
            netWorkDrawing.repaint();
        } catch (NumberFormatException ex) {
            return;
        }
        String calculatedPrice = String.valueOf(network.calculatePrice());
        totalPriceLabelValue.setText(calculatedPrice);
        String calculatedAvailability = String.valueOf(network.calculateAvailability());
        totalAvailabilityLabelValue.setText(calculatedAvailability);
        //For text appearance
//        componentList.setText(network.toString());
    }


    private void OpenFile() {
        //from https://stackoverflow.com/questions/40255039/how-to-choose-file-in-java
        JFileChooser chooser = new JFileChooser();

        //Set default open location
        File saveDirectory = new File(
                System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" +
                        System.getProperty("file.separator") + "NerdygadgetsFiles");
        //If the directory does not exist, make it
        boolean result = saveDirectory.mkdir();
        chooser.setCurrentDirectory(saveDirectory);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            File file = chooser.getSelectedFile();
            //from http://www.java2s.com/Tutorials/Java/Swing_How_to/JFileChooser/Display_the_Contents_of_a_text_file_in_a_JTextArea.htm
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                componentList.read(input, "READING FILE...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        //Set the default save directory
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        File saveDirectory = new File(
                System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" +
                        System.getProperty("file.separator") + "NerdygadgetsFiles");
        //If the directory does not exist, make it
        saveDirectory.mkdir();
        fileChooser.setCurrentDirectory(saveDirectory);

        //region Save as txt
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            try {
                componentList.write(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        //endregion

        //region Save as class
//        int option = fileChooser.showSaveDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            if (file == null) {
//                return;
//            }
//            try {
//                FileOutputStream fileOut = new FileOutputStream(saveDirectory + "testa.asd");
//                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
//                objectOut.writeObject(network);
//                objectOut.close();
//                System.out.println("The Object  was succesfully written to a file");
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
        //endregion
    }

    //    public static class FrameDragListener extends MouseAdapter {
//        //From https://stackoverflow.com/questions/16046824/making-a-java-swing-frame-movable-and-setundecorated
//
//        private final JFrame frame;
//        private Point mouseDownCompCoords = null;
//
//        public FrameDragListener(JFrame frame) {
//            this.frame = frame;
//        }
//
//        public void mouseReleased(MouseEvent e) {
//            mouseDownCompCoords = null;
//        }
//
//        public void mousePressed(MouseEvent e) {
//            mouseDownCompCoords = e.getPoint();
//        }
//
//        public void mouseDragged(MouseEvent e) {
//            Point currCoords = e.getLocationOnScreen();
//            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
//        }
//    }
}
