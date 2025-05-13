package com.parkingManagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Сущность записи о парковке в системе управления парковкой.
 */
@Entity
@Table(name = "parking_record")
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_space_id", nullable = false)
    private ParkingSpace parkingSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    /**
     * Конструктор по умолчанию для Hibernate.
     */
    public ParkingRecord() {
    }

    /**
     * Конструктор для создания записи о парковке.
     *
     * @param id            идентификатор записи
     * @param parkingSpace  парковочное место
     * @param vehicle       автомобиль
     * @param client        клиент
     * @param entryTime     время въезда
     * @param exitTime      время выезда
     */
    public ParkingRecord(Long id, ParkingSpace parkingSpace, Vehicle vehicle, Client client,
                         LocalDateTime entryTime, LocalDateTime exitTime) {
        this.id = id;
        this.parkingSpace = parkingSpace;
        this.vehicle = vehicle;
        this.client = client;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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