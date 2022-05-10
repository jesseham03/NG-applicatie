import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.awt.Toolkit.getDefaultToolkit;

public class Frame extends JFrame implements ActionListener {
    private final Network network = new Network();

    //region JUIcomponents
    private final JButton addNewComponentButton;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField availabilityField;

    private final JLabel totalPriceLabelValue;
    private final JLabel totalAvailabilityLabelValue;

    private JTextArea componentList;

    private final JMenuItem openMonitoringButton;

    private final JMenu monitoringButton;

    private final JMenu optimisationButton;

    private final JMenuItem openFileButton;
    private final JMenuItem saveFileButton;
    private final JMenuItem quitButton;

    private final JComboBox<InfrastructureComponent.Type> typeComboBox;

//    private DragDropPanel visualNetwork;

    private final JPanel netWorkDrawing;
    private final JPanel bottomPanel;
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //region Menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu m1 = new JMenu("Start");
        monitoringButton = new JMenu("Monitoring");
        optimisationButton= new JMenu("Optimisation");
        menuBar.add(m1);
        menuBar.add(monitoringButton);
        menuBar.add(optimisationButton);
        openMonitoringButton = new JMenuItem("Open");
        openFileButton = new JMenuItem("Open");
        saveFileButton = new JMenuItem("Save as");
        quitButton = new JMenuItem("Quit");
        openMonitoringButton.addActionListener(this);
        openFileButton.addActionListener(this);
        saveFileButton.addActionListener(this);
        quitButton.addActionListener(this);
        optimisationButton.addActionListener(this);
        m1.add(openFileButton);
        m1.add(saveFileButton);
        m1.add(quitButton);
        monitoringButton.add(openMonitoringButton);

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
        System.out.println("0");
        if (e.getSource() == addNewComponentButton) {
            CreateComponent();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        } else if (e.getSource() == openFileButton) {
            OpenFile();
        } else if (e.getSource() == saveFileButton) {
            saveToFile();
        } else if(e.getSource() == openMonitoringButton){
            System.out.println("1");
            OpenMonitoring();
        } else if(e.getSource() == optimisationButton) {

            OpenOptimisation();
        }
    }


    private void OpenMonitoring(){
        System.out.println("2");
        try{
            JPanel Monitoring = new JPanel();
            Monitoring.setLayout(new GridLayout(1,2));

            JPanel MonitoringInfo = new JPanel();
            MonitoringInfo.setLayout(new GridLayout(6,1));
            JButton RefreshButton = new JButton("Refresh");
            JLabel InfoName = new JLabel("Serve Name: Server");
            JLabel InfoAvailability = new JLabel("Availability: Available");
            JLabel InfoTimeAvailabality = new JLabel("Uptime: 15:12");
            JLabel InfoProcessing = new JLabel("Processing Power Used: 50%");
            JLabel InfoDisk = new JLabel("Disk Usage: 40GB/64GB");


            MonitoringInfo.add(InfoName);
            MonitoringInfo.add(InfoAvailability);
            MonitoringInfo.add(InfoTimeAvailabality);
            MonitoringInfo.add(InfoProcessing);
            MonitoringInfo.add(InfoDisk);
            MonitoringInfo.add(RefreshButton);
            RefreshButton.addActionListener(this);



            JPanel MonitoringBar = new JPanel();
            MonitoringBar.setPreferredSize(new Dimension(200,450));
            MonitoringBar.setLayout(new GridLayout(3,1));
            JScrollPane sp = new JScrollPane();
            sp.setViewportView(MonitoringBar);
            sp.getVerticalScrollBar().setUnitIncrement(50);
//            MonitoringBar.add(sp);

//            MonitoringBar.add(new MonitoringScroll(Color.magenta));
//            MonitoringBar.add(new MonitoringScroll(Color.cyan));
//            MonitoringBar.add(new MonitoringScroll(Color.blue));


            setVisible(true);


            Monitoring.add(MonitoringBar);
            Monitoring.add(MonitoringInfo);




            getContentPane().add(BorderLayout.CENTER, Monitoring);




            bottomPanel.setVisible(false);
            netWorkDrawing.setVisible(false);






            setVisible(true);

        } catch(Exception e){
            System.out.println("happetee");
            return;
        }
    }


    private void OpenOptimisation(){
        try{
            JPanel Optimisation = new JPanel();

            setVisible(true );
        } catch(Exception e){
            return;
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
