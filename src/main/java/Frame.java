import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Frame extends JFrame implements ActionListener {

    private Network network = new Network();

    //region JUIcomponents
    private JButton addNewComponentButton;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField availabilityField;

    private JLabel totalPriceLabelValue;
    private JLabel totalAvailabilityLabelValue;

    private JTextArea componentList;

    private JMenuItem openFileButton;
    private JMenuItem saveFileButton;
    private JMenuItem quitButton;


    private JComboBox<InfrastructureComponent.Type> typeComboBox;
    //endregion

    private DragDropPanel visualNetwork;

    public Frame() {
        try {
//            setUndecorated(true);
            com.formdev.flatlaf.FlatDarculaLaf.setup();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setTitle("Application");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //region Menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Help");
        menuBar.add(m1);
        menuBar.add(m2);
        openFileButton = new JMenuItem("Open");
        saveFileButton = new JMenuItem("Save as");
        quitButton = new JMenuItem("Quit");
        openFileButton.addActionListener(this);
        saveFileButton.addActionListener(this);
        quitButton.addActionListener(this);
        m1.add(openFileButton);
        m1.add(saveFileButton);
        m1.add(quitButton);
        //endregion

        //region Bottombar
        JPanel bottomPanel = new JPanel();

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

        //For visual appearance
//        visualNetwork = new DragDropPanel(network);

        //For text appearance
        componentList = new JTextArea();
        componentList.setEditable(false);

        getContentPane().add(BorderLayout.SOUTH, bottomPanel);
        getContentPane().add(BorderLayout.NORTH, menuBar);
        //For visual appearance
//        getContentPane().add(BorderLayout.CENTER, visualNetwork);
        //For text appearance
        getContentPane().add(BorderLayout.CENTER, componentList);

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
        }
        else if (e.getSource() == quitButton) {
            System.exit(0);
        }
        else if (e.getSource() == openFileButton) {
            OpenFile();
        }
        else if (e.getSource() == saveFileButton) {
            saveToFile();
        }
    }

    private void CreateComponent() {
        try {
            float price = Float.parseFloat(priceField.getText());
            float availability = Float.parseFloat(availabilityField.getText());
            InfrastructureComponent.Type selectedType = (InfrastructureComponent.Type) typeComboBox.getSelectedItem();
            network.addComponent(new InfrastructureComponent(nameField.getText(), price, availability, selectedType));
        } catch (NumberFormatException ex) {
            return;
        }
        String calculatedPrice = String.valueOf(network.calculatePrice());
        totalPriceLabelValue.setText(calculatedPrice);
        String calculatedAvailability = String.valueOf(network.calculateAvailability());
        totalAvailabilityLabelValue.setText(calculatedAvailability);

        //For text appearance
        componentList.setText(network.toString());
        //For visual appearance
//            repaint();
    }

    private void OpenFile() {
        //from https://stackoverflow.com/questions/40255039/how-to-choose-file-in-java
        JFileChooser chooser = new JFileChooser();

        //Set default open location
        File saveDirectory = new File(
                System.getProperty("user.home") + System.getProperty("file.separator")+ "Documents" +
                        System.getProperty("file.separator")+"NerdygadgetsFiles");
        //If the directory does not exist, make it
        boolean result = saveDirectory.mkdir();
        chooser.setCurrentDirectory(saveDirectory);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
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
                System.getProperty("user.home") + System.getProperty("file.separator")+ "Documents" +
                        System.getProperty("file.separator")+"NerdygadgetsFiles");
        //If the directory does not exist, make it
        boolean result = saveDirectory.mkdir();
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

    public static class FrameDragListener extends MouseAdapter {
        //From https://stackoverflow.com/questions/16046824/making-a-java-swing-frame-movable-and-setundecorated

        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}
