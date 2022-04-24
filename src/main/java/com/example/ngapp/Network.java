package com.example.ngapp;

import java.util.ArrayList;

public class Network {
    private ArrayList<InfrastructureComponent> webServerComponents = new ArrayList<>();
    private ArrayList<InfrastructureComponent> databaseServerComponents = new ArrayList<>();
    private ArrayList<InfrastructureComponent> firewallComponents = new ArrayList<>();

    public void addComponent(InfrastructureComponent component){
        switch (component.getType()){
            case Web -> webServerComponents.add(component);
            case Database -> databaseServerComponents.add(component);
            case Firewall -> firewallComponents.add(component);
        }
    }

    public float calculatePrice(){
        float price = 0;
        for (InfrastructureComponent component : webServerComponents) {
            price += component.getCostInEuros();
        }
        for (InfrastructureComponent component : databaseServerComponents) {
            price += component.getCostInEuros();
        }
        for (InfrastructureComponent component : firewallComponents) {
            price += component.getCostInEuros();
        }
        return price;
    }

    public float calculateAvailability(){
        if (webServerComponents.isEmpty() || databaseServerComponents.isEmpty() || firewallComponents.isEmpty()) {
            return 0;
        }

        float webServerAvailability = 1;
        float dataBaseAvailability = 1;
        float firewallAvailability = 1;

        for (InfrastructureComponent component : webServerComponents) {
            webServerAvailability *= 1 - component.getAvailabilityPercentage();
        }
        webServerAvailability = (1 - webServerAvailability);

        for (InfrastructureComponent component : databaseServerComponents) {
            dataBaseAvailability *= 1 - component.getAvailabilityPercentage();
        }
        dataBaseAvailability = (1 - dataBaseAvailability);

        for (InfrastructureComponent component : firewallComponents) {
            firewallAvailability *= 1 - component.getAvailabilityPercentage();
        }
        firewallAvailability = (1 - firewallAvailability);

        return firewallAvailability * dataBaseAvailability * webServerAvailability;
    }

    public static void main(String[] args) {
        Network network1 = new Network();
        InfrastructureComponent webServer1 = new InfrastructureComponent("naam1", 20, 0.9f, InfrastructureComponent.Type.Web);
        InfrastructureComponent webServer2 = new InfrastructureComponent("naam1", 20, 0.9f, InfrastructureComponent.Type.Web);
        InfrastructureComponent webServer3 = new InfrastructureComponent("naam1", 20, 0.9f, InfrastructureComponent.Type.Web);
        InfrastructureComponent webServer4 = new InfrastructureComponent("naam1", 20, 0.95f, InfrastructureComponent.Type.Web);
        InfrastructureComponent dataBase1 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent database2 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent database3 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent database4 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent database5 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent database6 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent database7 = new InfrastructureComponent("naam1", 20, 0.8f, InfrastructureComponent.Type.Database);
        InfrastructureComponent fireWall1 = new InfrastructureComponent("naam1", 20, 0.99998f, InfrastructureComponent.Type.Firewall);
        network1.addComponent(webServer1);
        network1.addComponent(webServer2);
        network1.addComponent(webServer3);
        network1.addComponent(webServer4);
        network1.addComponent(dataBase1);
        network1.addComponent(database2);
        network1.addComponent(database3);
        network1.addComponent(database4);
        network1.addComponent(database5);
        network1.addComponent(database6);
        network1.addComponent(database7);
        network1.addComponent(fireWall1);
        System.out.println(network1.calculateAvailability());
        System.out.println(network1.calculatePrice());
    }
}
