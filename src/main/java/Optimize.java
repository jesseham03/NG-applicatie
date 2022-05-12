//import java.util.ArrayList;
//
//public class Optimize {
//    public ArrayList<InfrastructureComponent> calculateCheapest(Network input, double requiredUptime) {
//        Network network = new Network();
//
//        double currentUptime = network.calculateAvailability();
//
//        Network bestNetwork = null;
//        findBest(input, requiredUptime, network, bestNetwork);
//
//        while (network.calculateAvailability() < requiredUptime) {
//            for (InfrastructureComponent component : possibleComponents) {
//                network.addComponent(component);
//            }
//        }
//        return new ArrayList<>();
//    }
//
//    private void findBest(Network input, double requiredUptime, Network network, Network bestNetwork) {
//        for (InfrastructureComponent component : input.getWebServerComponents()) {
//            network.addComponent(component);
//            if (network.calculateAvailability() < requiredUptime) {
//                continue;
//            }
//            if (bestNetwork == null || network.calculatePrice() < bestNetwork.calculatePrice()) {
//                bestNetwork = network.clone();
//            }
//            network.removeComponent(component);
//        }
//    }
//}
