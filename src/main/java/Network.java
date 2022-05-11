import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Network implements Serializable {

    private final List<InfrastructureComponent> webServerComponents = new ArrayList<>();
    private final List<InfrastructureComponent> databaseServerComponents = new ArrayList<>();
    private final List<InfrastructureComponent> firewallComponents = new ArrayList<>();

    //region Getters
    public List<InfrastructureComponent> getAllComponentsCopy() {
        List<InfrastructureComponent> tempList = new ArrayList<>();
        tempList.addAll(webServerComponents);
        tempList.addAll(databaseServerComponents);
        tempList.addAll(firewallComponents);
        return tempList;
    }
    //endregion

    public void addComponent(InfrastructureComponent component) {
        switch (component.getType()) {
            case Web -> webServerComponents.add(component);
            case Database -> databaseServerComponents.add(component);
            case Firewall -> firewallComponents.add(component);
        }
    }

    //Json formatting
//    @Override
//    public String toString() {
//        JsonObject networkObject = new JsonObject();
//
//        JsonObject Webservers = new JsonObject();
//        for (InfrastructureComponent component : webServerComponents) {
//            JsonObject web = new JsonObject();
//            web.addProperty("Price", component.getCostInEuros());
//            web.addProperty("Availability", component.getAvailability());
//
//            Webservers.add(component.getComponentName(), web);
//        }
//        networkObject.add("Webservers", Webservers);
//
//
//        JsonObject Databases = new JsonObject();
//        for (InfrastructureComponent component : databaseServerComponents) {
//            JsonObject database = new JsonObject();
//            database.addProperty("Price", component.getCostInEuros());
//            database.addProperty("Availability", component.getAvailability());
//
//            Databases.add(component.getComponentName(), database);
//        }
//        networkObject.add("Databases", Databases);
//
//
//        JsonObject FireWalls = new JsonObject();
//        for (InfrastructureComponent component : firewallComponents) {
//            JsonObject firewall = new JsonObject();
//            firewall.addProperty("Price", component.getCostInEuros());
//            firewall.addProperty("Availability", component.getAvailability());
//
//            FireWalls.add(component.getComponentName(), firewall);
//        }
//        networkObject.add("Firewalls", FireWalls);
//
//        return String.valueOf(networkObject);
//    }

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
            webServerAvailability *= 1 - component.getAvailability();
        }
        webServerAvailability = (1 - webServerAvailability);

        for (InfrastructureComponent component : databaseServerComponents) {
            dataBaseAvailability *= 1 - component.getAvailability();
        }
        dataBaseAvailability = (1 - dataBaseAvailability);

        for (InfrastructureComponent component : firewallComponents) {
            firewallAvailability *= 1 - component.getAvailability();
        }
        firewallAvailability = (1 - firewallAvailability);

        return firewallAvailability * dataBaseAvailability * webServerAvailability;
    }
}
