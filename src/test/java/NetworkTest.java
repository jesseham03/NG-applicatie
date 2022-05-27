import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworkTest {
    @Test
    public void testNetworkCreation() {
        Network network = new Network();
        network.addComponent(new InfrastructureComponent());
        network.addComponent(new InfrastructureComponent("test", 2500, 99, ComponentType.Web, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent(null, 0, 1000, ComponentType.Database, null, null));
        assertEquals(3, network.getAllComponentsCopy().size());
    }

    @Test
    public void duplicateComponents() {
        Network network = new Network();
        network.addComponent(new InfrastructureComponent("test", 2500, 99, ComponentType.Web, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 2500, 99, ComponentType.Web, "192.168.1.1", null));
        assertEquals(2, network.getAllComponentsCopy().size());
    }

    @Test
    public void calculateEmptyPrice() {
        Network network = new Network();
        assertEquals(0, network.calculatePrice(), 0.01);
    }

    @Test
    public void calculateHighPrice() {
        Network network = new Network();
        int nComponents = 10;
        for (int i = 0; i < nComponents; i++) {
            network.addComponent(new InfrastructureComponent("test", 100000, 99, ComponentType.Web, "192.168.1.1", null));
        }
        assertEquals(nComponents * 100000, network.calculatePrice(), 0.01);
    }

    @Test
    public void calculateZeroAvailability() {
        Network network = new Network();
        network.addComponent(new InfrastructureComponent("test", 100000, 0, ComponentType.Web, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 100000, 0, ComponentType.Web, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 100000, 0, ComponentType.Database, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 100000, 0, ComponentType.Firewall, "192.168.1.1", null));
        assertEquals(0, network.calculateAvailability(), 0.01);
    }

    @Test
    public void calculateHighAvailability() {
        Network network = new Network();
        network.addComponent(new InfrastructureComponent("test", 100000, 99, ComponentType.Web, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 100000, 100, ComponentType.Web, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 100000, 101, ComponentType.Database, "192.168.1.1", null));
        network.addComponent(new InfrastructureComponent("test", 100000, 1000, ComponentType.Firewall, "192.168.1.1", null));
        assertEquals(1010, network.calculateAvailability(), 0.01);
    }

    @Test
    public void calculateSingleComponentAvailability() {
        Network network = new Network();
        network.addComponent(new InfrastructureComponent("test", 100000, 99, ComponentType.Web, "192.168.1.1", null));
        assertEquals(network.calculateAvailability(), 0, 0.01);
    }
}
