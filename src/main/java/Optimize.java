import java.util.ArrayList;

public class Optimize {
    public ArrayList<InfrastructureComponent> calculateCheapest(ArrayList<InfrastructureComponent> possibleComponents, double requiredUptime) {
        Network network = new Network();

        double currentUptime = network.calculateAvailability();

        while (network.calculateAvailability() < requiredUptime) {
            for (InfrastructureComponent component : possibleComponents) {
                network.addComponent(component);
            }
        }
        return new ArrayList<>();
    }
}
