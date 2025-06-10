package com.parkingManagement.controller;

import com.parkingManagement.model.Vehicle;
import com.parkingManagement.service.ClientService;
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

/**
 * Контроллер для обработки HTTP-запросов, связанных с автомобилями.
 */
@Controller
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final ClientService clientService;

    /**
     * Конструктор контроллера автомобилей.
     
     * @param vehicleService сервис для управления автомобилями
     * @param clientService сервис для управления клиентами
     */
    @Autowired
    public VehicleController(VehicleService vehicleService, ClientService clientService) {
        this.vehicleService = vehicleService;
        this.clientService = clientService;
    }

    /**
     * Отображает список всех автомобилей.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для отображения списка автомобилей
     */
    @GetMapping
    public String listVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "vehicle/list";
    }

    /**
     * Отображает форму для создания нового автомобиля.
     
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы автомобиля
     */
    @GetMapping("/new")
    public String newVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("clients", clientService.getAllClients());
        return "vehicle/form";
    }

    /**
     * Создаёт новый автомобиль на основе данных из формы.
     
     * @param vehicle данные автомобиля из формы
     * @param clientId идентификатор клиента
     * @param model модель для передачи данных в представление
     * @return редирект на список автомобилей или страница ошибки
     */
    @PostMapping
    public String createVehicle(@ModelAttribute Vehicle vehicle, @RequestParam("clientId") Long clientId, Model model) {
        try {
            vehicle.setClient(clientService.getClient(clientId));
            vehicleService.createVehicle(vehicle);
            return "redirect:/vehicles";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает информацию об автомобиле по его идентификатору.
     
     * @param id идентификатор автомобиля
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для просмотра автомобиля или страница ошибки
     */
    @GetMapping("/{id}")
    public String viewVehicle(@PathVariable Long id, Model model) {
        try {
            Vehicle vehicle = vehicleService.getVehicle(id);
            model.addAttribute("vehicle", vehicle);
            return "vehicle/view";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает форму для редактирования автомобиля.
     
     * @param id идентификатор автомобиля
     * @param model модель для передачи данных в представление
     * @return имя JSP-страницы для формы автомобиля или страница ошибки
     */
    @GetMapping("/{id}/edit")
    public String editVehicleForm(@PathVariable Long id, Model model) {
        try {
            Vehicle vehicle = vehicleService.getVehicle(id);
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("clients", clientService.getAllClients());
            return "vehicle/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Обновляет данные автомобиля на основе формы.
     
     * @param id идентификатор автомобиля
     * @param vehicle данные автомобиля из формы
     * @param clientId идентификатор клиента
     * @param model модель для передачи данных в представление
     * @return редирект на список автомобилей или страница ошибки
     */
    @PostMapping("/{id}")
    public String updateVehicle(@PathVariable Long id, @ModelAttribute Vehicle vehicle, @RequestParam("clientId") Long clientId, Model model) {
        try {
            vehicle.setId(id);
            vehicle.setClient(clientService.getClient(clientId));
            vehicleService.updateVehicle(vehicle);
            return "redirect:/vehicles";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Удаляет автомобиль по идентификатору.
     
     * @param id идентификатор автомобиля
     * @param model модель для передачи данных в представление
     * @return редирект на список автомобилей или страница ошибки
     */
    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id, Model model) {
        try {
            vehicleService.deleteVehicle(id);
            return "redirect:/vehicles";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}