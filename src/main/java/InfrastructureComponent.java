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
    private double costInEuros = 0;
    private double availability = 0;
    private ComponentType type = ComponentType.Web;
    @Builder.Default
    private String hostname = "127.0.0.1";

    private Point location;

    public void setLocation(Point location) {
        this.location = location;
    }
}
