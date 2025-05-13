package com.parkingManagement.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность автомобиля в системе управления парковкой.
 */
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "model", length = 50)
    private String model;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParkingRecord> parkingRecords = new ArrayList<>();

    /**
     * Конструктор по умолчанию для Hibernate.
     */
    public Vehicle() {
    }

    /**
     * Конструктор для создания автомобиля.
     *
     * @param id           идентификатор автомобиля
     * @param client       клиент, владеющий автомобилем
     * @param licensePlate регистрационный номер
     * @param brand        марка автомобиля
     * @param model        модель автомобиля
     */
    public Vehicle(Long id, Client client, String licensePlate, String brand, String model) {
        this.id = id;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public List<ParkingRecord> getParkingRecords() {
        return parkingRecords;
    }

    public void setParkingRecords(List<ParkingRecord> parkingRecords) {
        this.parkingRecords = parkingRecords;
    }
}