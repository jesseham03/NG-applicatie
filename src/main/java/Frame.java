import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.awt.BorderLayout.*;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Runtime.getRuntime;
import static java.lang.Thread.sleep;
import static java.math.RoundingMode.CEILING;

public class Frame extends JFrame implements ActionListener {

    public static Frame frame;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();

    private final JLabel monitoringName;
    private final JLabel monitoringAvailability;
    private final JLabel monitoringUptime;
    private final JLabel monitoringCpu;
    private final JLabel monitoringDisk;
    private final JLabel monitoringPrice;
    private final JLabel monitoringLastUpdate;
    private final JLabel monitoringHost;

    private final JTextField hostnameField;

    private InfrastructureComponent monitoredComponent;

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

    private final JLabel errorLabel;
    private final JLabel totalPriceLabelValue;
    private final JLabel totalAvailabilityLabelValue;

    private final JMenuItem openFileButton;
    private final JMenuItem loadCurrentNetworkButton;
    private final JMenuItem loadDefaultNetworkButton;
    private final JMenuItem saveFileButton;
    private final JMenuItem quitButton;

    private final JComboBox<ComponentType> typeComboBox;

    private final JPanel netWorkDrawing;

    private final Thread diskThread;
    private final Thread cpuThread;
    private final Thread uptimeThread;
    //endregion

    public Frame() {
        frame = this;
        //region Frame setup
        try {
            com.formdev.flatlaf.FlatDarculaLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Splash splash = new Splash();

        setIconImage(getDefaultToolkit().getImage(getClass().getResource("/Favicon2.png")));
        setTitle("NG Network-Application");
        setSize(650, 450);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //endregion

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(darkerUIColor);

        //region Menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu start = new JMenu("Start");
        JMenu load = new JMenu("Load");
        menuBar.add(start);
        menuBar.add(load);
        openFileButton = new JMenuItem("Open");
        saveFileButton = new JMenuItem("Save as");
        quitButton = new JMenuItem("Quit");
        loadCurrentNetworkButton = new JMenuItem("Current network");
        loadDefaultNetworkButton = new JMenuItem("Default network");
        openFileButton.addActionListener(this);
        saveFileButton.addActionListener(this);
        quitButton.addActionListener(this);
        loadCurrentNetworkButton.addActionListener(this);
        loadDefaultNetworkButton.addActionListener(this);
        start.add(openFileButton);
        start.add(saveFileButton);
        start.add(quitButton);
        load.add(loadCurrentNetworkButton);
        load.add(loadDefaultNetworkButton);
        //endregion

        JPanel monitoringtab = new JPanel();
        monitoringtab.setLayout(new BorderLayout());
        JPanel monitoringComponentsPanel = createComponentPanel((b, c) -> prefillComponent(c), "/currentnetwork.json");
        monitoringComponentsPanel.setBackground(darkerUIColor);

        //region NetworkTab
        JPanel networkTab = new JPanel();
        networkTab.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(darkerUIColor);

        JPanel newComponentPanel = new JPanel();
        newComponentPanel.setBackground(darkerUIColor);
        nameField = new JTextField(15);
        hostnameField = new JTextField(5);
        priceField = new JTextField(5);
        setNumbersOnly(priceField);
        availabilityField = new JTextField(5);
        setNumbersOnly(availabilityField);
        typeComboBox = new JComboBox<>(ComponentType.values());
        addNewComponentButton = new JButton("Add");
        addNewComponentButton.addActionListener(this);
        uptimeField = new JTextField(5);
        setNumbersOnly(availabilityField);
        uptimeField.setText("90");
        optimizeButton = new JButton("Optimize");
        optimizeButton.addActionListener(this::optimize);
        errorLabel = new JLabel("");

        newComponentPanel.add(new JLabel("Name"));
        newComponentPanel.add(nameField);
        newComponentPanel.add(new JLabel("Host"));
        newComponentPanel.add(hostnameField);
        newComponentPanel.add(new JLabel("Price"));
        newComponentPanel.add(priceField);
        newComponentPanel.add(new JLabel("Availability"));
        newComponentPanel.add(availabilityField);
        newComponentPanel.add(typeComboBox);
        newComponentPanel.add(addNewComponentButton);
        newComponentPanel.add(new JLabel("Availability"));
        newComponentPanel.add(uptimeField);
        newComponentPanel.add(optimizeButton);
        newComponentPanel.add(errorLabel);

        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(darkerUIColor);
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
        monitoringInfo.setLayout(new GridLayout(9, 2));
        monitoringInfo.setBackground(darkerUIColor);

        monitoringInfo.add(new JLabel("Name: "));
        monitoringName = new JLabel("");
        monitoringInfo.add(monitoringName);

        monitoringInfo.add(new JLabel("Host: "));
        monitoringHost = new JLabel("");
        monitoringInfo.add(monitoringHost);

        monitoringInfo.add(new JLabel("Availability: "));
        monitoringAvailability = new JLabel("");
        monitoringInfo.add(monitoringAvailability);

        monitoringInfo.add(new JLabel("Price: "));
        monitoringPrice = new JLabel("");
        monitoringInfo.add(monitoringPrice);

        monitoringInfo.add(new JLabel("Uptime: "));
        monitoringUptime = new JLabel("");
        monitoringInfo.add(monitoringUptime);

        monitoringInfo.add(new JLabel("Cpu usage: "));
        monitoringCpu = new JLabel("");
        monitoringInfo.add(monitoringCpu);

        monitoringInfo.add(new JLabel("Disk usage: "));
        monitoringDisk = new JLabel("");
        monitoringInfo.add(monitoringDisk);

        monitoringInfo.add(new JLabel("Last updated: "));
        monitoringLastUpdate = new JLabel("");
        monitoringInfo.add(monitoringLastUpdate);

        refreshButton = new JButton("Refresh");
        monitoringInfo.add(refreshButton);
        refreshButton.addActionListener(this);

        monitoring.add(monitoringInfo);

        JPanel componentsPanel = createComponentPanel((b, c) -> prefillComponent(c), "/defaultcomponents.json");
        componentsPanel.setBackground(darkerUIColor);

        networkTab.add(WEST, new JScrollPane(componentsPanel));
        networkTab.add(CENTER, netWorkDrawing);
        networkTab.add(SOUTH, bottomPanel);
        monitoringtab.add(WEST, new JScrollPane(monitoringComponentsPanel));
        monitoringtab.add(CENTER, monitoring);
        //endregion

        getContentPane().add(BorderLayout.NORTH, menuBar);
        tabbedPane.addTab("Design and Optimize", networkTab);
        tabbedPane.addTab("Monitoring", monitoringtab);
        getContentPane().add(tabbedPane);

        setLocationRelativeTo(null);

        setVisible(true);
        //TODO juiste commando's gebruiken
        diskThread = startMonitoring(monitoringDisk, "df | awk '{print $4}' | sed -n '2 p'");
        cpuThread = startMonitoring(monitoringCpu, "top -bn1 | awk '{print $4}' | sed -n '3 p'");
        uptimeThread = startMonitoring(monitoringUptime, "uptime | awk '{print $3 \" \" $4}' | sed 's/.$//'");
    }

    //functions
    private void optimize(ActionEvent actionEvent) {
        try {
            double requiredUptime = Double.parseDouble(uptimeField.getText());
            if (requiredUptime > 99.9999999d) {
                setError("Impossible uptime");
                return;
            }
            if (network == null || network.getAllComponentsCopy().isEmpty()) {
                setError("Network is empty");
                return;
            }
            if (network.getWebServerComponents().isEmpty() || network.getDatabaseServerComponents().isEmpty() || network.getFirewallComponents().isEmpty()) {
                setError("Not all types of components added");
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
            setError("");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setError(String error) {
        errorLabel.setText(error);
        errorLabel.setForeground(errorColor);
    }

    private JPanel createComponentPanel(BiConsumer<JButton, InfrastructureComponent> buttonFunction, String jsonFile) {
        JPanel panel = createPanel(0);
        try (InputStream stream = getClass().getResourceAsStream(jsonFile)) {
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
        hostnameField.setText(component.getHostname());
        priceField.setText(String.valueOf(component.getCostInEuros()));
        availabilityField.setText(String.valueOf(component.getAvailability()));
        typeComboBox.setSelectedItem(component.getType());
    }

    private void setNumbersOnly(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((c >= '0' && c <= '9') || c == '.' || c == ',' || c == KeyEvent.VK_BACK_SPACE) {
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
        } else if (e.getSource() == loadCurrentNetworkButton) {
            try {
                network = readFromJson(getClass().getClassLoader().getResourceAsStream("currentnetwork.json"));
                RegenerateNetworkDrawing();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == loadDefaultNetworkButton) {
            try {
                network = readFromJson(getClass().getClassLoader().getResourceAsStream("defaultcomponents.json"));
                RegenerateNetworkDrawing();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void refresh() {
        refreshButton.setText("Refreshing...");
        diskThread.interrupt();
        cpuThread.interrupt();
        uptimeThread.interrupt();
    }

    private Thread startMonitoring(JLabel label, String command) {

        //Start a new thread, so the application doesn't freeze
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    if (monitoredComponent == null) {
                        sleep(30_000);
                        continue;
                    }
                    String host = monitoredComponent.getHostname();
                    System.out.println("Starting Command on host: " + host);
//                    Process proc = getRuntime().exec(new String[]{"ssh", host, command});
                    Process proc = getRuntime().exec(new String[]{"ssh", "student@" + host, command});
                    String data = read(proc.getInputStream());
                    System.out.println("Command Tried1");
                    System.out.print(data + "\n");
                    if (proc.waitFor() != 0) {
                        read(proc.getErrorStream());
                        System.err.print(data + "\n");
                    }
                    label.setText(data);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    monitoringLastUpdate.setText(dtf.format(LocalDateTime.now()));
                    sleep(10_000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        sleep(10_000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } finally {
                    System.out.println("Command Tried");
                    refreshButton.setText("Refresh");
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        return thread;
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

    private void createComponent() {
        try {
            double price = Double.parseDouble(priceField.getText());
            double availability = Double.parseDouble(availabilityField.getText());
            ComponentType selectedType = (ComponentType) typeComboBox.getSelectedItem();
            if (network == null) {
                network = new Network();
            }

            if (network.getAllComponentsCopy().stream().anyMatch(c -> nameField.getText().equals(c.getComponentName()))) {
                setError("Component already exist with name: " + nameField.getText());

//                throw new IllegalArgumentException("Component already exist with name: " + nameField.getText());
                return;
            }
            InfrastructureComponent addedComponent = new InfrastructureComponent(nameField.getText(), price, availability, selectedType, hostnameField.getText(), null);
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
        visualComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDetails(addedComponent);
            }
        });
        netWorkDrawing.repaint();
    }

    private void saveToFile() {

        //Set the default save directory
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        File saveDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "NerdygadgetsFiles");

        //If the directory does not exist, make it
        saveDirectory.mkdir();
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
    }

    public void showDetails(InfrastructureComponent component) {
        monitoringName.setText(component.getComponentName());
        monitoringHost.setText(component.getHostname());
        monitoringAvailability.setText(String.valueOf(component.getAvailability()));
        monitoringPrice.setText(String.valueOf(component.getCostInEuros()));
        monitoredComponent = component;
        refresh();
    }
}
