import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfrastructureComponent {
    enum Type {
        Web,
        Database,
        Firewall
    }

    private String componentName;
    private float costInEuros;
    private float availability;
    private Type type;

    private Point location;

    public void setLocation(Point location) {
        this.location = location;
    }
}
