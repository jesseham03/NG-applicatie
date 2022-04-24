package com.example.ngapp;

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

    public InfrastructureComponent(String componentName, float costInEuros, float availability, Type type) {
        this.componentName = componentName;
        this.costInEuros = costInEuros;
        this.availability = availability;
        this.type = type;
    }
}
