import javax.swing.*;
import java.io.Serializable;

//Implements serializable is necessary to write this to a file
public class InfrastructureComponent extends JComponent implements Serializable {

    enum Type {
        Web,
        Database,
        Firewall
    }

    private final String componentName;
    private final float costInEuros;
    private final float availability;
    private final Type type;

    //region Getters
    public float getCostInEuros() {
        return costInEuros;
    }

    public float getAvailabilityPercentage() {
        return availability;
    }

    public String getComponentName() {
        return componentName;
    }

    public Type getType() {
        return type;
    }
    //endregion

    public String toString() {
        return componentName + "(" + type + ") - " + costInEuros + " | " + availability;
    }

    public InfrastructureComponent(String componentName, float costInEuros, float availability, Type type) {
        this.componentName = componentName;
        this.costInEuros = costInEuros;
        this.availability = availability;
        this.type = type;
    }
}
