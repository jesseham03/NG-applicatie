package com.example.ngapp;

public class InfrastructureComponent {
    private String componentName;
    private float costInEuros;
    private float availabilityPercentage;

    public float getCostInEuros() {
        return costInEuros;
    }

    public float getAvailabilityPercentage() {
        return availabilityPercentage;
    }

    public String getComponentName() {
        return componentName;
    }

    public InfrastructureComponent(String componentName, float costInEuros, float availabilityPercentage){
        this.componentName = componentName;
        this.costInEuros = costInEuros;
        this.availabilityPercentage = availabilityPercentage;
    }
}
