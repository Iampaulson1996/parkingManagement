package com.parkingManagement.controller;

import com.parkingManagement.model.ParkingLot;
import com.parkingManagement.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Контроллер для обработки HTTP-запросов, связанных с парковками.
 */
@Controller
@RequestMapping("/parkingLots")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    /**
     * Конструктор контроллера парковок.
     
     * @param parkingLotService сервис для управления парковками
     */
    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    /**
     * Отображает список всех парковок.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для отображения списка парковок
     */
    @GetMapping
    public String listParkingLots(Model model) {
        model.addAttribute("parkingLots", parkingLotService.getAllParkingLots());
        return "parkingLot/list";
    }

    /**
     * Отображает форму для создания новой парковки.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы парковки
     */
    @GetMapping("/new")
    public String newParkingLotForm(Model model) {
        model.addAttribute("parkingLot", new ParkingLot());
        return "parkingLot/form";
    }

    /**
     * Создаёт новую парковку на основе данных из формы.
     
     * @param parkingLot данные парковки из формы
     * @param model модель для передачи данных в представление
     * @return редирект на список парковок или страница ошибки
     */
    @PostMapping
    public String createParkingLot(@ModelAttribute ParkingLot parkingLot, Model model) {
        try {
            parkingLotService.createParkingLot(parkingLot);
            return "redirect:/parkingLots";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает информацию о парковке по её идентификатору.
     
     * @param id идентификатор парковки
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для просмотра парковки или страница ошибки
     */
    @GetMapping("/{id}")
    public String viewParkingLot(@PathVariable Long id, Model model) {
        try {
            ParkingLot parkingLot = parkingLotService.getParkingLot(id);
            model.addAttribute("parkingLot", parkingLot);
            return "parkingLot/view";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает форму для редактирования парковки.
     
     * @param id идентификатор парковки
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы парковки или страница ошибки
     */
    @GetMapping("/{id}/edit")
    public String editParkingLotForm(@PathVariable Long id, Model model) {
        try {
            ParkingLot parkingLot = parkingLotService.getParkingLot(id);
            model.addAttribute("parkingLot", parkingLot);
            return "parkingLot/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Обновляет данные парковки на основе формы.
     
     * @param id идентификатор парковки
     * @param parkingLot данные парковки из формы
     * @param model модель для передачи данных в представление
     * @return редирект на список парковок или страница ошибки
     */
    @PostMapping("/{id}")
    public String updateParkingLot(@PathVariable Long id, @ModelAttribute ParkingLot parkingLot, Model model) {
        try {
            parkingLot.setId(id);
            parkingLotService.updateParkingLot(parkingLot);
            return "redirect:/parkingLots";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Удаляет парковку по идентификатору.
     
     * @param id идентификатор парковки
     * @param model модель для передачи данных в представление
     * @return редирект на список парковок или страница ошибки
     */
    @PostMapping("/{id}/delete")
    public String deleteParkingLot(@PathVariable Long id, Model model) {
        try {
            parkingLotService.deleteParkingLot(id);
            return "redirect:/parkingLots";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}