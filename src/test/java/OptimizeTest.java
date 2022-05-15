import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptimizeTest {

    @Test
    public void emptyNetwork() {
        Network input = new Network();
        Network best = new Optimize().calculateCheapest(input, 0.99d);
        assertNull(best);
    }

    @Test
    public void eachTypeOneComponent() {
        Network input = new Network();
        addEachComponentType(input);
        Network best = new Optimize().calculateCheapest(input, 0.99d * 0.99d * 0.99d * 100d);
        assertEquals(input, best);
    }

    @Test
    public void eachTypeMultipleComponents() {
        Network input = new Network();
        addEachComponentType(input);
        Network expected = addEachComponentType(input.copy());
        Network best = new Optimize().calculateCheapest(input, expected.calculateAvailability());
        assertEquals(expected, best);
    }

    private Network addEachComponentType(Network network) {
        network.addComponent(new InfrastructureComponent("firewall1", 1, 99d, ComponentType.Firewall, null, null));
        network.addComponent(new InfrastructureComponent("db1", 1, 99d, ComponentType.Database, null, null));
        network.addComponent(new InfrastructureComponent("web1", 1, 99d, ComponentType.Web, null, null));
        return network;
    }

}