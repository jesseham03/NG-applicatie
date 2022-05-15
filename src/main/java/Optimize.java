import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparing;

public class Optimize {
    public Network calculateCheapest(Network input, double requiredUptime) {
        // Sort to optimize the optimizing
        input.getFirewallComponents().sort(compareFunction());
        input.getWebServerComponents().sort(compareFunction());
        input.getDatabaseServerComponents().sort(compareFunction());
        return findBest(input, requiredUptime, new Network(), null);
    }

    private Comparator<InfrastructureComponent> compareFunction() {
        return comparing(InfrastructureComponent::getAvailability).reversed();
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
