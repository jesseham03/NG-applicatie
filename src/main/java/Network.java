import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Network implements Serializable {

    private List<InfrastructureComponent> webServerComponents = new ArrayList<>();
    private List<InfrastructureComponent> databaseServerComponents = new ArrayList<>();
    private List<InfrastructureComponent> firewallComponents = new ArrayList<>();

    public List<InfrastructureComponent> getAllComponentsCopy() {
        List<InfrastructureComponent> tempList = new ArrayList<>();
        tempList.addAll(webServerComponents);
        tempList.addAll(databaseServerComponents);
        tempList.addAll(firewallComponents);
        return tempList;
    }

    public void addComponent(InfrastructureComponent component) {
        switch (component.getType()) {
            case Web -> webServerComponents.add(component);
            case Database -> databaseServerComponents.add(component);
            case Firewall -> firewallComponents.add(component);
        }
    }

    public void removeComponent(InfrastructureComponent component) {
        switch (component.getType()) {
            case Web -> webServerComponents.remove(component);
            case Database -> databaseServerComponents.remove(component);
            case Firewall -> firewallComponents.remove(component);
        }
    }

    public double calculatePrice() {
        double price = 0;
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

    public double calculateAvailability() {
        if (webServerComponents.isEmpty() || databaseServerComponents.isEmpty() || firewallComponents.isEmpty()) {
            return 0;
        }

        return 100d * getAvailability(firewallComponents) * getAvailability(databaseServerComponents) * getAvailability(webServerComponents);
    }

    private double getAvailability(List<InfrastructureComponent> webServerComponents) {
        double availability = 1d;
        for (InfrastructureComponent component : webServerComponents) {
            availability *= 1d - component.getAvailability() / 100d;
        }
        return 1d - availability;
    }

    public Network copy() {
        return new Network(new ArrayList<>(webServerComponents), new ArrayList<>(databaseServerComponents), new ArrayList<>(firewallComponents));
    }
}
