package com.parkingManagement.controller;

import com.parkingManagement.model.ParkingSpace;
import com.parkingManagement.service.ParkingLotService;
import com.parkingManagement.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для обработки HTTP-запросов, связанных с парковочными местами.
 */
@Controller
@RequestMapping("/parkingSpaces")
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;
    private final ParkingLotService parkingLotService;

    /**
     * Конструктор контроллера парковочных мест.
     
     * @param parkingSpaceService сервис для управления парковочными местами
     * @param parkingLotService сервис для управления парковками
     */
    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService, ParkingLotService parkingLotService) {
        this.parkingSpaceService = parkingSpaceService;
        this.parkingLotService = parkingLotService;
    }

    /**
     * Отображает список всех парковочных мест.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для отображения списка парковочных мест
     */
    @GetMapping
    public String listParkingSpaces(Model model) {
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return "parkingSpace/list";
    }

    /**
     * Отображает форму для создания нового парковочного места.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы парковочного места
     */
    @GetMapping("/new")
    public String newParkingSpaceForm(Model model) {
        model.addAttribute("parkingSpace", new ParkingSpace());
        model.addAttribute("parkingLots", parkingLotService.getAllParkingLots());
        return "parkingSpace/form";
    }

    /**
     * Создаёт новое парковочное место на основе данных из формы.
     
     * @param parkingSpace данные парковочного места из формы
     * @param parkingLotId идентификатор парковки
     * @param model модель для передачи данных в представление
     * @return редирект на список парковочных мест или страница ошибки
     */
    @PostMapping
    public String createParkingSpace(@ModelAttribute ParkingSpace parkingSpace, @RequestParam("parkingLotId") Long parkingLotId, Model model) {
        try {
            parkingSpace.setParkingLot(parkingLotService.getParkingLot(parkingLotId));
            parkingSpaceService.createParkingSpace(parkingSpace);
            return "redirect:/parkingSpaces";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает информацию о парковочном месте по его идентификатору.
     
     * @param id идентификатор парковочного места
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для просмотра парковочного места или страница ошибки
     */
    @GetMapping("/{id}")
    public String viewParkingSpace(@PathVariable Long id, Model model) {
        try {
            ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(id);
            model.addAttribute("parkingSpace", parkingSpace);
            return "parkingSpace/view";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает форму для редактирования парковочного места.
     
     * @param id идентификатор парковочного места
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы парковочного места или страница ошибки
     */
    @GetMapping("/{id}/edit")
    public String editParkingSpaceForm(@PathVariable Long id, Model model) {
        try {
            ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(id);
            model.addAttribute("parkingSpace", parkingSpace);
            model.addAttribute("parkingLots", parkingLotService.getAllParkingLots());
            return "parkingSpace/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Обновляет данные парковочного места на основе формы.
     
     * @param id идентификатор парковочного места
     * @param parkingSpace данные парковочного места из формы
     * @param parkingLotId идентификатор парковки
     * @param model модель для передачи данных в представление
     * @return редирект на список парковочных мест или страница ошибки
     */
    @PostMapping("/{id}")
    public String updateParkingSpace(@PathVariable Long id, @ModelAttribute ParkingSpace parkingSpace, @RequestParam("parkingLotId") Long parkingLotId, Model model) {
        try {
            parkingSpace.setId(id);
            parkingSpace.setParkingLot(parkingLotService.getParkingLot(parkingLotId));
            parkingSpaceService.updateParkingSpace(parkingSpace);
            return "redirect:/parkingSpaces";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Удаляет парковочное место по идентификатору.
     
     * @param id идентификатор парковочного места
     * @param model модель для передачи данных в представление
     * @return редирект на список парковочных мест или страница ошибки
     */
    @PostMapping("/{id}/delete")
    public String deleteParkingSpace(@PathVariable Long id, Model model) {
        try {
            parkingSpaceService.deleteParkingSpace(id);
            return "redirect:/parkingSpaces";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}