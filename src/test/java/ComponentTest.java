import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ComponentTest {
    @Test
    public void emptyComponentDefaultValue() {
        InfrastructureComponent component1 = new InfrastructureComponent();
        InfrastructureComponent component2 = new InfrastructureComponent(null, 0, 0, ComponentType.Web, "127.0.0.1", null);
        assertEquals(component1, component2);
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();

    @Test
    public void loadingComponentFromJson() {
        try (InputStream stream = getClass().getResourceAsStream("/defaultcomponents.json")) {
            Network defaultNetwork = OBJECT_READER.readValue(stream, Network.class);
            InfrastructureComponent component = new InfrastructureComponent("HAL9001W", 2200.0, 80, ComponentType.Web, "127.0.0.1", null);
            assertTrue(defaultNetwork.getWebServerComponents().contains(component));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setLocation() {
        InfrastructureComponent component2 = new InfrastructureComponent(null, 0, 0, ComponentType.Web, "127.0.0.1", null);
        component2.setLocation(new Point(10, 10));
        assertEquals(component2.getLocation(), new Point(10, 10));
    }
}
