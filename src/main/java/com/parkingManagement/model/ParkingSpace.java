package com.parkingManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность парковочного места в системе управления парковкой.
 */
@Entity
@Table(name = "parking_space")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @Column(name = "space_number", nullable = false, length = 10)
    private String spaceNumber;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @OneToMany(mappedBy = "parkingSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingRecord> parkingRecords = new ArrayList<>();

    /**
     * Конструктор по умолчанию для Hibernate.
     */
    public ParkingSpace() {
    }

    /**
     * Конструктор для создания парковочного места.

     * @param id          идентификатор парковочного места
     * @param parkingLot  парковка, к которой относится место
     * @param spaceNumber номер парковочного места
     * @param type        тип парковочного места
     */
    public ParkingSpace(Long id, ParkingLot parkingLot, String spaceNumber, String type) {
        this.id = id;
        this.parkingLot = parkingLot;
        this.spaceNumber = spaceNumber;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public String getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(String spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ParkingRecord> getParkingRecords() {
        return parkingRecords;
    }

    public void setParkingRecords(List<ParkingRecord> parkingRecords) {
        this.parkingRecords = parkingRecords;
    }
}