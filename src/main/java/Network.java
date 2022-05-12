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

        return getAvailability(firewallComponents) * getAvailability(databaseServerComponents) * getAvailability(webServerComponents);
    }

    private float getAvailability(List<InfrastructureComponent> webServerComponents) {
        float availability = 1;
        for (InfrastructureComponent component : webServerComponents) {
            availability *= 1 - component.getAvailability();
        }
        return 1 - availability;
    }
}
