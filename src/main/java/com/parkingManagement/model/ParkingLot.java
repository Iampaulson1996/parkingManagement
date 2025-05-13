package com.parkingManagement.model;

/**
 * Модель парковки в системе управления парковкой.
 */
public class ParkingLot {
    private Long id;
    private String name;
    private String address;
    private Integer capacity;

    /**
     * Конструктор для создания парковки.
     *
     * @param id       идентификатор парковки
     * @param name     название парковки
     * @param address  адрес парковки
     * @param capacity вместимость парковки
     */
    public ParkingLot(Long id, String name, String address, Integer capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}