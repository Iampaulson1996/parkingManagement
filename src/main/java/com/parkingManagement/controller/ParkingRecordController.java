package com.parkingManagement.controller;

import com.parkingManagement.model.ParkingRecord;
import com.parkingManagement.service.ClientService;
import com.parkingManagement.service.ParkingRecordService;
import com.parkingManagement.service.ParkingSpaceService;
import com.parkingManagement.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Контроллер для обработки HTTP-запросов, связанных с записями о парковке.
 */
@Controller
@RequestMapping("/parkingRecords")
public class ParkingRecordController {
    private final ParkingRecordService parkingRecordService;
    private final ParkingSpaceService parkingSpaceService;
    private final VehicleService vehicleService;
    private final ClientService clientService;

    /**
     * Конструктор контроллера записей о парковке.
     
     * @param parkingRecordService сервис для управления записями о парковке
     * @param parkingSpaceService сервис для управления парковочными местами
     * @param vehicleService сервис для управления автомобилями
     * @param clientService сервис для управления клиентами
     */
    @Autowired
    public ParkingRecordController(ParkingRecordService parkingRecordService, ParkingSpaceService parkingSpaceService,
                                   VehicleService vehicleService, ClientService clientService) {
        this.parkingRecordService = parkingRecordService;
        this.parkingSpaceService = parkingSpaceService;
        this.vehicleService = vehicleService;
        this.clientService = clientService;
    }

    /**
     * Отображает список всех записей о парковке.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для отображения списка записей
     */
    @GetMapping
    public String listParkingRecords(Model model) {
        model.addAttribute("parkingRecords", parkingRecordService.getAllParkingRecords());
        return "parkingRecord/list";
    }

    /**
     * Отображает форму для создания новой записи о парковке.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы записи
     */
    @GetMapping("/new")
    public String newParkingRecordForm(Model model) {
        model.addAttribute("parkingRecord", new ParkingRecord());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("clients", clientService.getAllClients());
        return "parkingRecord/form";
    }

    /**
     * Создаёт новую запись о парковке на основе данных из формы.
     
     * @param parkingRecord данные записи о парковке из формы
     * @param parkingSpaceId идентификатор парковочного места
     * @param vehicleId идентификатор автомобиля
     * @param clientId идентификатор клиента
     * @param entryTime время въезда
     * @param model модель для передачи данных в представление
     * @return редирект на список записей или страница ошибки
     */
    @PostMapping
    public String createParkingRecord(@ModelAttribute ParkingRecord parkingRecord,
                                      @RequestParam("parkingSpaceId") Long parkingSpaceId,
                                      @RequestParam("vehicleId") Long vehicleId,
                                      @RequestParam("clientId") Long clientId,
                                      @RequestParam("entryTime") String entryTime,
                                      Model model) {
        try {
            parkingRecord.setParkingSpace(parkingSpaceService.getParkingSpace(parkingSpaceId));
            parkingRecord.setVehicle(vehicleService.getVehicle(vehicleId));
            parkingRecord.setClient(clientService.getClient(clientId));
            parkingRecord.setEntryTime(LocalDateTime.parse(entryTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            parkingRecordService.createParkingRecord(parkingRecord);
            return "redirect:/parkingRecords";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает информацию о записи о парковке по её идентификатору.
     
     * @param id идентификатор записи
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для просмотра записи или страница ошибки
     */
    @GetMapping("/{id}")
    public String viewParkingRecord(@PathVariable Long id, Model model) {
        try {
            ParkingRecord parkingRecord = parkingRecordService.getParkingRecord(id);
            model.addAttribute("parkingRecord", parkingRecord);
            return "parkingRecord/view";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает форму для редактирования записи о парковке.
     
     * @param id идентификатор записи
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы записи или страница ошибки
     */
    @GetMapping("/{id}/edit")
    public String editParkingRecordForm(@PathVariable Long id, Model model) {
        try {
            ParkingRecord parkingRecord = parkingRecordService.getParkingRecord(id);
            model.addAttribute("parkingRecord", parkingRecord);
            model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            model.addAttribute("clients", clientService.getAllClients());
            return "parkingRecord/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Обновляет данные записи о парковке на основе формы.
     
     * @param id идентификатор записи
     * @param parkingRecord данные записи о парковке из формы
     * @param parkingSpaceId идентификатор парковочного места
     * @param vehicleId идентификатор автомобиля
     * @param clientId идентификатор клиента
     * @param entryTime время въезда
     * @param exitTime время выезда (может быть null)
     * @param model модель для передачи данных в представление
     * @return редирект на список записей или страница ошибки
     */
    @PostMapping("/{id}")
    public String updateParkingRecord(@PathVariable Long id, @ModelAttribute ParkingRecord parkingRecord,
                                      @RequestParam("parkingSpaceId") Long parkingSpaceId,
                                      @RequestParam("vehicleId") Long vehicleId,
                                      @RequestParam("clientId") Long clientId,
                                      @RequestParam("entryTime") String entryTime,
                                      @RequestParam(value = "exitTime", required = false) String exitTime,
                                      Model model) {
        try {
            parkingRecord.setId(id);
            parkingRecord.setParkingSpace(parkingSpaceService.getParkingSpace(parkingSpaceId));
            parkingRecord.setVehicle(vehicleService.getVehicle(vehicleId));
            parkingRecord.setClient(clientService.getClient(clientId));
            parkingRecord.setEntryTime(LocalDateTime.parse(entryTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if (exitTime != null && !exitTime.isEmpty()) {
                parkingRecord.setExitTime(LocalDateTime.parse(exitTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            parkingRecordService.updateParkingRecord(parkingRecord);
            return "redirect:/parkingRecords";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Удаляет запись о парковке по идентификатору.
     
     * @param id идентификатор записи
     * @param model модель для передачи данных в представление
     * @return редирект на список записей или страница ошибки
     */
    @PostMapping("/{id}/delete")
    public String deleteParkingRecord(@PathVariable Long id, Model model) {
        try {
            parkingRecordService.deleteParkingRecord(id);
            return "redirect:/parkingRecords";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}