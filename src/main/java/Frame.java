import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.awt.BorderLayout.*;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Runtime.getRuntime;
import static java.math.RoundingMode.CEILING;

public class Frame extends JFrame implements ActionListener {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();

    private Network network = new Network();

    //region JUIcomponents
    private final Color darkerUIColor = new Color(40, 40, 40);
    private final Color errorColor = new Color(237, 67, 55);

    private final JButton addNewComponentButton;
    private final JButton optimizeButton;
    private final JButton refreshButton;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField availabilityField;
    private final JTextField uptimeField;
    private final JLabel optimizeErrorLabel;

    private final JLabel totalPriceLabelValue;
    private final JLabel totalAvailabilityLabelValue;

    private final JMenuItem openFileButton;
    private final JMenuItem saveFileButton;
    private final JMenuItem quitButton;

    private final JComboBox<ComponentType> typeComboBox;

    private final JPanel netWorkDrawing;
    private final JPanel bottomPanel;
//    private final JPanel monitoring;

//    private final JScrollPane scrollpane;

    public static Map<String, ImageIcon> imageMap = null;

    JLabel infoDisk;
    private final java.util.List<InfrastructureComponent> selectedComponents = new ArrayList<>();
    //endregion

    public Frame() {
        //region Frame setup
        try {
            com.formdev.flatlaf.FlatDarculaLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setIconImage(getDefaultToolkit().getImage(getClass().getResource("/Favicon2.png")));
        setTitle("NG Network-Application");
        setSize(650, 450);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUIFont(new javax.swing.plaf.FontUIResource("Roboto", Font.PLAIN, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //endregion

        //region Menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu start = new JMenu("Start");
        menuBar.add(start);
        openFileButton = new JMenuItem("Open");
        saveFileButton = new JMenuItem("Save as");
        quitButton = new JMenuItem("Quit");
        openFileButton.addActionListener(this);
        saveFileButton.addActionListener(this);
        quitButton.addActionListener(this);
        start.add(openFileButton);
        start.add(saveFileButton);
        start.add(quitButton);
        //endregion

        //region NetworkTab
        JPanel networkTab = new JPanel();
        networkTab.setLayout(new BorderLayout());

        bottomPanel = new JPanel();
        bottomPanel.setBackground(darkerUIColor);

        JPanel newComponentPanel = new JPanel();
        JLabel newComponentLabel = new JLabel("New Component");
        nameField = new JTextField(15);
        JLabel priceLabel = new JLabel("Price");
        priceField = new JTextField(5);
        setNumbersOnly(priceField, true);
        JLabel availabilityLabel = new JLabel("Availability");
        availabilityField = new JTextField(5);
        setNumbersOnly(availabilityField, true);
        typeComboBox = new JComboBox<>(ComponentType.values());
        addNewComponentButton = new JButton("Add");
        addNewComponentButton.addActionListener(this);
        JLabel uptimeLabel = new JLabel("Availability");
        uptimeField = new JTextField(5);
        setNumbersOnly(availabilityField, true);
        uptimeField.setText("90");
        optimizeButton = new JButton("Optimize");
        optimizeButton.addActionListener(this::optimize);
        optimizeErrorLabel = new JLabel("");

        newComponentPanel.add(newComponentLabel);
        newComponentPanel.add(nameField);
        newComponentPanel.add(priceLabel);
        newComponentPanel.add(priceField);
        newComponentPanel.add(availabilityLabel);
        newComponentPanel.add(availabilityField);
        newComponentPanel.add(typeComboBox);
        newComponentPanel.add(addNewComponentButton);
        newComponentPanel.add(uptimeLabel);
        newComponentPanel.add(uptimeField);
        newComponentPanel.add(optimizeButton);
        newComponentPanel.add(optimizeErrorLabel);

        JPanel statsPanel = new JPanel();
        JLabel totalPriceLabel = new JLabel("Price:");
        totalPriceLabelValue = new JLabel("0");
        JLabel totalAvailabilityLabel = new JLabel("Availability:");
        totalAvailabilityLabelValue = new JLabel("0");
        statsPanel.add(totalPriceLabel);
        statsPanel.add(totalPriceLabelValue);
        statsPanel.add(totalAvailabilityLabel);
        statsPanel.add(totalAvailabilityLabelValue);

        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.add(statsPanel);
        bottomPanel.add(newComponentPanel);

        netWorkDrawing = new JPanel();
        netWorkDrawing.setLayout(null);

        JPanel monitoring = new JPanel();
        monitoring.setLayout(new FlowLayout());
        monitoring.setBackground(darkerUIColor);

        JPanel monitoringInfo = new JPanel();
        monitoringInfo.setLayout(new GridLayout(6, 1));

        JLabel infoName = new JLabel("Server Name: " + MonitoringScroll.getComponentName());
        JLabel infoAvailability = new JLabel("Availability: " + MonitoringScroll.getAvailability());
        JLabel infoTimeAvailabality = new JLabel("Uptime: " + MonitoringScroll.getUptime());
        JLabel infoProcessing = new JLabel("Processing Power Used: " + MonitoringScroll.getProcessing());
        infoDisk = new JLabel("Disk Usage: " + MonitoringScroll.getDiskUsage());
        refreshButton = new JButton("Refresh");

        monitoringInfo.add(infoName);
        monitoringInfo.add(infoAvailability);
        monitoringInfo.add(infoTimeAvailabality);
        monitoringInfo.add(infoProcessing);
        monitoringInfo.add(infoDisk);
        monitoringInfo.add(refreshButton);
        refreshButton.addActionListener(this);

        monitoring.add(monitoringInfo);

        JPanel componentsPanel = createComponentPanel((b, c) -> prefillComponent(c));
        componentsPanel.setBackground(darkerUIColor);

        networkTab.add(WEST, new JScrollPane(componentsPanel));
        networkTab.add(CENTER, netWorkDrawing);
        networkTab.add(SOUTH, bottomPanel);
        networkTab.add(EAST, monitoringInfo);
        //endregion

        getContentPane().add(BorderLayout.NORTH, menuBar);
        getContentPane().add(networkTab);

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void optimize(ActionEvent actionEvent) {
        try {
            double requiredUptime = Double.parseDouble(uptimeField.getText());
            if (requiredUptime > 99.9999999d) {
                setOptimizeError("Impossible uptime");
                return;
            }
            if (network == null || network.getAllComponentsCopy().isEmpty()) {
                setOptimizeError("Network is empty");
                return;
            }
            if (network.getWebServerComponents().isEmpty() || network.getDatabaseServerComponents().isEmpty() || network.getFirewallComponents().isEmpty()) {
                setOptimizeError("Not all types of components added");
                return;
            }
            optimizeButton.setText("Optimizing...");
            optimizeButton.setEnabled(false);

            Thread thread = new Thread(() -> {
                System.out.println("Optimizing started");
                network = new Optimize().calculateCheapest(network, requiredUptime);
                optimizeButton.setText("Optimize");
                optimizeButton.setEnabled(true);
                RegenerateNetworkDrawing();
                System.out.println("Optimizing finished: " + network);
            });

            thread.start();
            setOptimizeError("");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setOptimizeError(String error) {
        optimizeErrorLabel.setText(error);
        optimizeErrorLabel.setForeground(errorColor);
    }

    private JPanel createComponentPanel(BiConsumer<JButton, InfrastructureComponent> buttonFunction) {
        JPanel panel = createPanel(0);
        try (InputStream stream = getClass().getResourceAsStream("/defaultcomponents.json")) {
            Network defaultNetwork = readFromJson(stream);
            for (InfrastructureComponent component : defaultNetwork.getAllComponentsCopy()) {
                addComponentButton(buttonFunction, panel, component);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return panel;
    }

    private JPanel createPanel(int rows) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, 1));
        return panel;
    }

    private void addComponentButton(BiConsumer<JButton, InfrastructureComponent> buttonFunction, JPanel panel, InfrastructureComponent component) {
        JButton button = new JButton(component.getComponentName());
        button.setPreferredSize(new Dimension(250, 5));
        button.addActionListener(b -> buttonFunction.accept(button, component));
        panel.add(button);
    }

    private void prefillComponent(InfrastructureComponent component) {
        nameField.setText(component.getComponentName());
        priceField.setText(String.valueOf(component.getCostInEuros()));
        availabilityField.setText(String.valueOf(component.getAvailability()));
        typeComboBox.setSelectedItem(component.getType());
    }

    private void setNumbersOnly(JTextField field, boolean isDouble) {
        field.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c >= '0') && (c <= '9')) || (isDouble && (c == '.')) || (isDouble && (c == ',')) || (c == KeyEvent.VK_BACK_SPACE)) {
                    return;
                }
                e.consume();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addNewComponentButton) {
            createComponent();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        } else if (e.getSource() == openFileButton) {
            openFile();
        } else if (e.getSource() == saveFileButton) {
            saveToFile();
        } else if (e.getSource() == refreshButton) {
            refresh();
        }
    }

    private void refresh() {
        refreshButton.setText("Refreshing...");
        refreshButton.setEnabled(false);
        //Start a new thread, so the application doesn't freeze
        Thread thread = new Thread(() -> {
            System.out.println("Starting Command");
            String host = "192.168.1.101";
            String command = "df -H";
            try {
                Process proc = getRuntime().exec(new String[]{"ssh", host, command});
                String data = read(proc.getInputStream());
                System.out.println("Command Tried1");
                System.out.print(data + "\n");
                if (proc.waitFor() != 0) {
                    read(proc.getErrorStream());
                    System.err.print(data + "\n");
                }
                infoDisk.setText(data);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            System.out.println("Command Tried");
            refreshButton.setText("Refresh");
            refreshButton.setEnabled(true);
        });

        thread.start();
    }

    private String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder result = new StringBuilder();
        while (true) {
            String line;
            if ((line = reader.readLine()) == null) break;
            result.append(line).append('\n');
        }
        return result.toString();
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) UIManager.put(key, f);
        }
    }

    private void createComponent() {
        try {
            double price = Double.parseDouble(priceField.getText());
            double availability = Double.parseDouble(availabilityField.getText());
            ComponentType selectedType = (ComponentType) typeComboBox.getSelectedItem();
            if (network == null) {
                network = new Network();
            }

            if (network.getAllComponentsCopy().stream().anyMatch(c -> nameField.getText().equals(c.getComponentName()))) {
                throw new IllegalArgumentException("Component already exist with name: " + nameField.getText());
            }
            InfrastructureComponent addedComponent = new InfrastructureComponent(nameField.getText(), price, availability, selectedType, null);
            network.addComponent(addedComponent);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return;
        }
        RegenerateNetworkDrawing();
    }

    private void openFile() {
        //from https://stackoverflow.com/questions/40255039/how-to-choose-file-in-java
        JFileChooser chooser = new JFileChooser();

        //Set default open location
        File saveDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "NerdygadgetsFiles");
        //If the directory does not exist, make it
        saveDirectory.mkdir();
        chooser.setCurrentDirectory(saveDirectory);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON file", "json");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        File file = chooser.getSelectedFile();
        try {
            network = readFromJson(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegenerateNetworkDrawing();
    }

    private Network readFromJson(InputStream stream) throws IOException {
        return OBJECT_READER.readValue(stream, Network.class);
    }

    private void RegenerateNetworkDrawing() {
        String calculatedPrice = String.valueOf(network.calculatePrice());
        totalPriceLabelValue.setText(calculatedPrice);
        //Formatting the calculatedavailability
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(CEILING);
        totalAvailabilityLabelValue.setText(df.format(network.calculateAvailability()));
        netWorkDrawing.removeAll();
        for (InfrastructureComponent component : network.getAllComponentsCopy()) {
            drawComponent(component);
        }
    }

    private void drawComponent(InfrastructureComponent addedComponent) {
        DraggableImageComponent visualComponent = new DraggableImageComponent(addedComponent);
        netWorkDrawing.add(visualComponent);
        visualComponent.setOverbearing(true);

        int size = 75;
        if (addedComponent.getLocation() == null) {
            int centerX = netWorkDrawing.getWidth() / 2;
            int centerY = netWorkDrawing.getHeight() / 2;
            visualComponent.setLocation(centerX - visualComponent.getWidth() / 2, centerY - visualComponent.getHeight() / 2);
        } else {
            visualComponent.setLocation(addedComponent.getLocation());
        }
        visualComponent.setSize(size, size);
        netWorkDrawing.repaint();
    }

    private void saveToFile() {
        //Set the default save directory
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        File saveDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "NerdygadgetsFiles");
        //If the directory does not exist, make it
        boolean result = saveDirectory.mkdir();
        fileChooser.setCurrentDirectory(saveDirectory);

        //Save as Json
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }
            if (!file.getName().toLowerCase().endsWith(".json")) {
                file = new File(file.getParentFile(), file.getName() + ".json");
            }
            try (FileWriter myWriter = new FileWriter(file)) {
                String json = OBJECT_WRITER.writeValueAsString(network);
                myWriter.write(json);
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //region Save as txt
//        int option = fileChooser.showSaveDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            if (file == null) {
//                return;
//            }
//            if (!file.getName().toLowerCase().endsWith(".txt")) {
//                file = new File(file.getParentFile(), file.getName() + ".txt");
//            }
//            try {
//                componentList.write(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
//                Desktop.getDesktop().open(file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        //endregion
    }
}
