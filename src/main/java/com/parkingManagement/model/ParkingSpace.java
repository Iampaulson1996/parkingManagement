package com.parkingManagement.model;

/**
 * Модель парковочного места в системе управления парковкой.
 */
public class ParkingSpace {
    private Long id;
    private Long parkingLotId;
    private String spaceNumber;
    private String type;

    /**
     * Конструктор для создания парковочного места.
     *
     * @param id           идентификатор парковочного места
     * @param parkingLotId идентификатор парковки
     * @param spaceNumber  номер парковочного места
     * @param type         тип парковочного места (REGULAR/DISABLED/VIP)
     */
    public ParkingSpace(Long id, Long parkingLotId, String spaceNumber, String type) {
        this.id = id;
        this.parkingLotId = parkingLotId;
        this.spaceNumber = spaceNumber;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
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
}