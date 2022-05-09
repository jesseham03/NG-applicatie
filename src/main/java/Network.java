import java.io.Serializable;
import java.util.ArrayList;

public class Network implements Serializable {

    private final ArrayList<InfrastructureComponent> webServerComponents = new ArrayList<>();
    private final ArrayList<InfrastructureComponent> databaseServerComponents = new ArrayList<>();
    private final ArrayList<InfrastructureComponent> firewallComponents = new ArrayList<>();

    //region Getters
    public ArrayList<InfrastructureComponent> getAllComponentsCopy() {
        ArrayList<InfrastructureComponent> tempList = new ArrayList<>();
        tempList.addAll(webServerComponents);
        tempList.addAll(databaseServerComponents);
        tempList.addAll(firewallComponents);
        return tempList;
    }

    public ArrayList<InfrastructureComponent> getWebServerComponents() {
        return webServerComponents;
    }

    public ArrayList<InfrastructureComponent> getDatabaseServerComponents() {
        return databaseServerComponents;
    }

    public ArrayList<InfrastructureComponent> getFirewallComponents() {
        return firewallComponents;
    }
    //endregion

    public void addComponent(InfrastructureComponent component) {
        switch (component.getType()) {
            case Web -> webServerComponents.add(component);
            case Database -> databaseServerComponents.add(component);
            case Firewall -> firewallComponents.add(component);
        }
    }

    @Override
    public String toString() {
        StringBuilder infoString = new StringBuilder("All servers:\n");
        infoString.append("────────────────────────────────────────\n");
        for (InfrastructureComponent component : webServerComponents) {
            infoString.append("- ").append(component).append("\n");
        }
        infoString.append("────────────────────────────────────────\n");
        for (InfrastructureComponent component : databaseServerComponents) {
            infoString.append("- ").append(component).append("\n");
        }
        infoString.append("────────────────────────────────────────\n");
        for (InfrastructureComponent component : firewallComponents) {
            infoString.append("- ").append(component).append("\n");
        }
        return infoString.toString();
    }

    public float calculatePrice() {
        float price = 0;
        for (InfrastructureComponent component : webServerComponents) {
            price += component.getCostInEuros();
        }
        for (InfrastructureComponent component : databaseServerComponents) {
            price += component.getCostInEuros();
        }
        for (InfrastructureComponent component : firewallComponents) {
            price += component.getCostInEuros();
        }
        return price;
    }

    public float calculateAvailability() {
        if (webServerComponents.isEmpty() || databaseServerComponents.isEmpty() || firewallComponents.isEmpty()) {
            return 0;
        }

        float webServerAvailability = 1;
        float dataBaseAvailability = 1;
        float firewallAvailability = 1;

        for (InfrastructureComponent component : webServerComponents) {
            webServerAvailability *= 1 - component.getAvailabilityPercentage();
        }
        webServerAvailability = (1 - webServerAvailability);

        for (InfrastructureComponent component : databaseServerComponents) {
            dataBaseAvailability *= 1 - component.getAvailabilityPercentage();
        }
        dataBaseAvailability = (1 - dataBaseAvailability);

        for (InfrastructureComponent component : firewallComponents) {
            firewallAvailability *= 1 - component.getAvailabilityPercentage();
        }
        firewallAvailability = (1 - firewallAvailability);

        return firewallAvailability * dataBaseAvailability * webServerAvailability;
    }
}
