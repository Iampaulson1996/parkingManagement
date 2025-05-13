package com.parkingManagement.model;

import java.time.LocalDateTime;

/**
 * Модель записи о парковке в системе управления парковкой.
 */
public class ParkingRecord {
    private Long id;
    private Long parkingSpaceId;
    private Long vehicleId;
    private Long clientId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    /**
     * Конструктор для создания записи о парковке.
     *
     * @param id             идентификатор записи
     * @param parkingSpaceId идентификатор парковочного места
     * @param vehicleId      идентификатор автомобиля
     * @param clientId       идентификатор клиента
     * @param entryTime      время въезда
     * @param exitTime       время выезда
     */
    public ParkingRecord(Long id, Long parkingSpaceId, Long vehicleId, Long clientId, LocalDateTime entryTime,
                         LocalDateTime exitTime) {
        this.id = id;
        this.parkingSpaceId = parkingSpaceId;
        this.vehicleId = vehicleId;
        this.clientId = clientId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}