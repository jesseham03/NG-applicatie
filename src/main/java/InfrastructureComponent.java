import lombok.*;

import java.awt.*;

@Builder
@ToString
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class InfrastructureComponent {

    private String componentName;
    private double costInEuros;
    private double availability;
    private ComponentType type;

    private Point location;

    public void setLocation(Point location) {
        this.location = location;
    }
}
