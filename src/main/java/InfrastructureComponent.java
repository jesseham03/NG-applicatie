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
    @Builder.Default
    private String ipAddress = "127.0.0.1";

    private Point location;

    public void setLocation(Point location) {
        this.location = location;
    }
}
