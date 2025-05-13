package com.parkingManagement.model;

/**
 * Модель автомобиля в системе управления парковкой.
 */
public class Vehicle {
    private Long id;
    private Long clientId;
    private String licensePlate;
    private String brand;
    private String model;

    /**
     * Конструктор для создания автомобиля.
     *
     * @param id            идентификатор автомобиля
     * @param clientId      идентификатор клиента
     * @param licensePlate  регистрационный номер
     * @param brand         марка автомобиля
     * @param model         модель автомобиля
     */
    public Vehicle(Long id, Long clientId, String licensePlate, String brand, String model) {
        this.id = id;
        this.clientId = clientId;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}