import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Optimize {
    public Network calculateCheapest(Network input, double requiredUptime) {
        return findBest(input, requiredUptime, new Network(), null);
    }

    private Network findBest(Network input, double requiredUptime, Network network, Network bestNetwork) {
        Set<Network> nextNetworks = new HashSet<>();

        bestNetwork = findBest(requiredUptime, network, bestNetwork, nextNetworks, input.getWebServerComponents());
        bestNetwork = findBest(requiredUptime, network, bestNetwork, nextNetworks, input.getFirewallComponents());
        bestNetwork = findBest(requiredUptime, network, bestNetwork, nextNetworks, input.getDatabaseServerComponents());

        for (Network nextNetwork : nextNetworks) {
            bestNetwork = findBest(input, requiredUptime, nextNetwork, bestNetwork);
        }
        return bestNetwork;
    }

    private Network findBest(double requiredUptime, Network network, Network bestNetwork, Set<Network> nextNetworks, List<InfrastructureComponent> components) {
        for (InfrastructureComponent component : components) {
            network.addComponent(component);

            boolean isCheaper = bestNetwork == null || network.calculatePrice() < bestNetwork.calculatePrice();
            if (isCheaper) {
                if (network.calculateAvailability() < requiredUptime) {
                    nextNetworks.add(network.copy());
                } else {
                    bestNetwork = network.copy();
                }
            }
            network.removeComponent(component);
        }
        return bestNetwork;
    }
}
