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

    //For the drag and drop system
    private Vector2 position = new Vector2();

    public void setPosition(Vector2 position) {
        this.position = position;
    }

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

    public Vector2 getPosition() {
        return position;
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
