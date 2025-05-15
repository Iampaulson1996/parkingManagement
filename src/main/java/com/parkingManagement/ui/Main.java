package com.parkingManagement.ui;

import com.parkingManagement.dao.*;
import com.parkingManagement.model.*;
import com.parkingManagement.service.*;
import com.parkingManagement.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Главный класс консольного приложения для управления парковкой.
 */
public class Main {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static Scanner scanner;
    private static ParkingLotService parkingLotService;
    private static ParkingSpaceService parkingSpaceService;
    private static ClientService clientService;
    private static VehicleService vehicleService;
    private static ParkingRecordService parkingRecordService;

    public static void main(String[] args) {
        try {
            emf = HibernateUtil.getEntityManagerFactory();
            em = emf.createEntityManager();
            scanner = new Scanner(System.in);
            parkingLotService = new ParkingLotService(new ParkingLotDao(em));
            parkingSpaceService = new ParkingSpaceService(new ParkingSpaceDao(em));
            clientService = new ClientService(new ClientDao(em));
            vehicleService = new VehicleService(new VehicleDao(em));
            parkingRecordService = new ParkingRecordService(new ParkingRecordDao(em));

            runMainMenu();
        } catch (PersistenceException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
            if (scanner != null) scanner.close();
            HibernateUtil.shutdown();
        }
    }

    /**
     * Запускает главное меню приложения.
     */
    private static void runMainMenu() {
        while (true) {
            System.out.println("\n=================");
            System.out.println("Управление парковкой");
            System.out.println("=================");
            System.out.println("1. Парковки");
            System.out.println("2. Парковочные места");
            System.out.println("3. Клиенты");
            System.out.println("4. Автомобили");
            System.out.println("5. Записи о парковке");
            System.out.println("6. Выход");

            int choice = getIntInput("Выберите опцию: ");
            if (choice == 6) {
                System.out.println("Выход...");
                break;
            }
            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: неверная опция!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> manageEntity("Парковки", Main::manageParkingLots);
                    case 2 -> manageEntity("Парковочные места", Main::manageParkingSpaces);
                    case 3 -> manageEntity("Клиенты", Main::manageClients);
                    case 4 -> manageEntity("Автомобили", Main::manageVehicles);
                    case 5 -> manageEntity("Записи о парковке", Main::manageParkingRecords);
                }
            } catch (IllegalArgumentException | PersistenceException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Выполняет управление сущностью с обработкой ошибок.

     * @param entityName название сущности для отображения
     * @param handler    обработчик меню для сущности
     */
    private static void manageEntity(String entityName, Runnable handler) {
        System.out.println("\n=== " + entityName + " ===");
        handler.run();
    }

    /**
     * Считывает целое число от пользователя.

     * @param prompt приглашение для ввода
     * @return введённое число
     */
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Ошибка: число должно быть положительным!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число!");
            }
        }
    }

    /**
     * Считывает длинное целое число от пользователя.

     * @param prompt приглашение для ввода
     * @return введённое число
     */
    private static long getLongInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                long value = Long.parseLong(input);
                if (value <= 0) {
                    System.out.println("Ошибка: число должно быть положительным!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число!");
            }
        }
    }

    /**
     * Считывает строку от пользователя.

     * @param prompt     приглашение для ввода
     * @param allowEmpty разрешить пустой ввод
     * @return введённая строка
     */
    private static String getStringInput(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!allowEmpty && input.isEmpty()) {
                System.out.println("Ошибка: ввод не может быть пустым!");
                continue;
            }
            return input;
        }
    }

    /**
     * Считывает дату и время от пользователя.

     * @param prompt приглашение для ввода
     * @return объект LocalDateTime или null при ошибке
     */
    private static LocalDateTime getDateTimeInput(String prompt) {
        String input = getStringInput(prompt, false);
        try {
            return LocalDateTime.parse(input.replace(" ", "T"));
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: неверный формат времени (гггг-мм-дд чч:мм:сс)!");
            return null;
        }
    }

    /**
     * Обрезает строку до указанной длины.

     * @param str       строка
     * @param maxLength максимальная длина
     * @return обрезанная строка
     */
    private static String shorten(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Управляет операциями с парковками.
     */
    private static void manageParkingLots() {
        while (true) {
            displaySubMenu("Парковки");
            int choice = getIntInput("Выберите опцию: ");
            if (choice == 6) break;
            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: неверная опция!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        String name = getStringInput("Введите название: ", false);
                        String address = getStringInput("Введите адрес: ", false);
                        int capacity = getIntInput("Введите вместимость: ");
                        ParkingLot lot = new ParkingLot(null, name, address, capacity);
                        parkingLotService.createParkingLot(lot);
                        System.out.println("Парковка создана с ID: " + lot.getId());
                    }
                    case 2 -> {
                        long id = getLongInput("Введите ID парковки: ");
                        ParkingLot lot = parkingLotService.getParkingLot(id);
                        System.out.println("\nПарковка:");
                        System.out.println("ID: " + lot.getId());
                        System.out.println("Название: " + lot.getName());
                        System.out.println("Адрес: " + lot.getAddress());
                        System.out.println("Вместимость: " + lot.getCapacity());
                    }
                    case 3 -> {
                        List<ParkingLot> lots = parkingLotService.getAllParkingLots();
                        if (lots.isEmpty()) {
                            System.out.println("Парковки отсутствуют.");
                        } else {
                            System.out.println("\nСписок парковок:");
                            System.out.println("ID | Название       | Адрес          | Вместимость");
                            System.out.println("--|----------------|----------------|------------");
                            for (ParkingLot lot : lots) {
                                System.out.printf("%d | %-14s | %-14s | %d%n",
                                        lot.getId(), shorten(lot.getName(), 14),
                                        shorten(lot.getAddress(), 14), lot.getCapacity());
                            }
                        }
                    }
                    case 4 -> {
                        long id = getLongInput("Введите ID парковки: ");
                        String name = getStringInput("Введите новое название: ", false);
                        String address = getStringInput("Введите новый адрес: ", false);
                        int capacity = getIntInput("Введите новую вместимость: ");
                        ParkingLot lot = new ParkingLot(id, name, address, capacity);
                        parkingLotService.updateParkingLot(lot);
                        System.out.println("Парковка обновлена");
                    }
                    case 5 -> {
                        long id = getLongInput("Введите ID парковки: ");
                        String confirm = getStringInput("Подтвердите удаление (да/нет): ", false);
                        if (confirm.equalsIgnoreCase("да")) {
                            parkingLotService.deleteParkingLot(id);
                            System.out.println("Парковка удалена");
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Управляет операциями с парковочными местами.
     */
    private static void manageParkingSpaces() {
        while (true) {
            displaySubMenu("Парковочные места");
            int choice = getIntInput("Выберите опцию: ");
            if (choice == 6) break;
            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: неверная опция!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        long lotId = getLongInput("Введите ID парковки: ");
                        ParkingLot parkingLot = parkingLotService.getParkingLot(lotId);
                        String number = getStringInput("Введите номер места: ", false);
                        String type = getStringInput("Введите тип (REGULAR/DISABLED/VIP): ", false);
                        ParkingSpace space = new ParkingSpace(null, parkingLot, number, type);
                        parkingSpaceService.createParkingSpace(space);
                        System.out.println("Место создано с ID: " + space.getId());
                    }
                    case 2 -> {
                        long id = getLongInput("Введите ID места: ");
                        ParkingSpace space = parkingSpaceService.getParkingSpace(id);
                        System.out.println("\nПарковочное место:");
                        System.out.println("ID: " + space.getId());
                        System.out.println("Номер: " + space.getSpaceNumber());
                        System.out.println("Тип: " + space.getType());
                        System.out.println("ID парковки: " + space.getParkingLot().getId());
                    }
                    case 3 -> {
                        List<ParkingSpace> spaces = parkingSpaceService.getAllParkingSpaces();
                        if (spaces.isEmpty()) {
                            System.out.println("Места отсутствуют.");
                        } else {
                            System.out.println("\nСписок парковочных мест:");
                            System.out.println("ID | Номер | Тип    | ID парковки");
                            System.out.println("--|-------|--------|------------");
                            for (ParkingSpace space : spaces) {
                                System.out.printf("%d | %-7s | %-7s | %d%n",
                                        space.getId(), shorten(space.getSpaceNumber(), 7),
                                        shorten(space.getType(), 7), space.getParkingLot().getId());
                            }
                        }
                    }
                    case 4 -> {
                        long id = getLongInput("Введите ID места: ");
                        long lotId = getLongInput("Введите новый ID парковки: ");
                        ParkingLot parkingLot = parkingLotService.getParkingLot(lotId);
                        String number = getStringInput("Введите новый номер места: ", false);
                        String type = getStringInput("Введите новый тип: ", false);
                        ParkingSpace space = new ParkingSpace(id, parkingLot, number, type);
                        parkingSpaceService.updateParkingSpace(space);
                        System.out.println("Место обновлено");
                    }
                    case 5 -> {
                        long id = getLongInput("Введите ID места: ");
                        String confirm = getStringInput("Подтвердите удаление (да/нет): ", false);
                        if (confirm.equalsIgnoreCase("да")) {
                            parkingSpaceService.deleteParkingSpace(id);
                            System.out.println("Место удалено");
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Управляет операциями с клиентами.
     */
    private static void manageClients() {
        while (true) {
            displaySubMenu("Клиенты");
            int choice = getIntInput("Выберите опцию: ");
            if (choice == 6) break;
            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: неверная опция!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        String name = getStringInput("Введите имя: ", false);
                        String phone = getStringInput("Введите телефон: ", true);
                        String email = getStringInput("Введите email: ", true);
                        Client client = new Client(null, name, phone.isEmpty() ? null : phone,
                                email.isEmpty() ? null : email);
                        clientService.createClient(client);
                        System.out.println("Клиент создан с ID: " + client.getId());
                    }
                    case 2 -> {
                        long id = getLongInput("Введите ID клиента: ");
                        Client client = clientService.getClient(id);
                        System.out.println("\nКлиент:");
                        System.out.println("ID: " + client.getId());
                        System.out.println("Имя: " + client.getName());
                        System.out.println("Телефон: " + (client.getPhone() != null ? client.getPhone() : "N/A"));
                        System.out.println("Email: " + (client.getEmail() != null ? client.getEmail() : "N/A"));
                    }
                    case 3 -> {
                        List<Client> clients = clientService.getAllClients();
                        if (clients.isEmpty()) {
                            System.out.println("Клиенты отсутствуют.");
                        } else {
                            System.out.println("\nСписок клиентов:");
                            System.out.println("ID | Имя    | Телефон   | Email");
                            System.out.println("--|--------|-----------|------");
                            for (Client client : clients) {
                                System.out.printf("%d | %-7s | %-9s | %-10s%n",
                                        client.getId(),
                                        shorten(client.getName(), 7),
                                        shorten(client.getPhone() != null ? client.getPhone() : "N/A", 9),
                                        shorten(client.getEmail() != null ? client.getEmail() : "N/A", 10));
                            }
                        }
                    }
                    case 4 -> {
                        long id = getLongInput("Введите ID клиента: ");
                        String name = getStringInput("Введите новое имя: ", false);
                        String phone = getStringInput("Введите новый телефон: ", true);
                        String email = getStringInput("Введите новый email: ", true);
                        Client client = new Client(id, name, phone.isEmpty() ? null : phone,
                                email.isEmpty() ? null : email);
                        clientService.updateClient(client);
                        System.out.println("Клиент обновлён");
                    }
                    case 5 -> {
                        long id = getLongInput("Введите ID клиента: ");
                        String confirm = getStringInput("Подтвердите удаление (да/нет): ", false);
                        if (confirm.equalsIgnoreCase("да")) {
                            clientService.deleteClient(id);
                            System.out.println("Клиент удалён");
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Управляет операциями с автомобилями.
     */
    private static void manageVehicles() {
        while (true) {
            displaySubMenu("Автомобили");
            int choice = getIntInput("Выберите опцию: ");
            if (choice == 6) break;
            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: неверная опция!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        long clientId = getLongInput("Введите ID клиента: ");
                        Client client = clientService.getClient(clientId);
                        String licensePlate = getStringInput("Введите рег. номер: ", false);
                        String brand = getStringInput("Введите марку: ", true);
                        String model = getStringInput("Введите модель: ", true);
                        Vehicle vehicle = new Vehicle(null, client, licensePlate,
                                brand.isEmpty() ? null : brand, model.isEmpty() ? null : model);
                        vehicleService.createVehicle(vehicle);
                        System.out.println("Автомобиль создан с ID: " + vehicle.getId());
                    }
                    case 2 -> {
                        long id = getLongInput("Введите ID автомобиля: ");
                        Vehicle vehicle = vehicleService.getVehicle(id);
                        System.out.println("\nАвтомобиль:");
                        System.out.println("ID: " + vehicle.getId());
                        System.out.println("Рег. номер: " + vehicle.getLicensePlate());
                        System.out.println("Марка: " + (vehicle.getBrand() != null ? vehicle.getBrand() : "N/A"));
                        System.out.println("Модель: " + (vehicle.getModel() != null ? vehicle.getModel() : "N/A"));
                        System.out.println("ID клиента: " + vehicle.getClient().getId());
                    }
                    case 3 -> {
                        List<Vehicle> vehicles = vehicleService.getAllVehicles();
                        if (vehicles.isEmpty()) {
                            System.out.println("Автомобили отсутствуют.");
                        } else {
                            System.out.println("\nСписок автомобилей:");
                            System.out.println("ID | Рег. номер | Марка | Модель | ID клиента");
                            System.out.println("--|------------|-------|--------|-----------");
                            for (Vehicle vehicle : vehicles) {
                                System.out.printf("%d | %-10s | %-7s | %-7s | %d%n",
                                        vehicle.getId(),
                                        shorten(vehicle.getLicensePlate(), 10),
                                        shorten(vehicle.getBrand() != null ? vehicle.getBrand() : "N/A", 7),
                                        shorten(vehicle.getModel() != null ? vehicle.getModel() : "N/A", 7),
                                        vehicle.getClient().getId());
                            }
                        }
                    }
                    case 4 -> {
                        long id = getLongInput("Введите ID автомобиля: ");
                        long clientId = getLongInput("Введите новый ID клиента: ");
                        Client client = clientService.getClient(clientId);
                        String licensePlate = getStringInput("Введите новый рег. номер: ", false);
                        String brand = getStringInput("Введите новую марку: ", true);
                        String model = getStringInput("Введите новую модель: ", true);
                        Vehicle vehicle = new Vehicle(id, client, licensePlate,
                                brand.isEmpty() ? null : brand, model.isEmpty() ? null : model);
                        vehicleService.updateVehicle(vehicle);
                        System.out.println("Автомобиль обновлён");
                    }
                    case 5 -> {
                        long id = getLongInput("Введите ID автомобиля: ");
                        String confirm = getStringInput("Подтвердите удаление (да/нет): ", false);
                        if (confirm.equalsIgnoreCase("да")) {
                            vehicleService.deleteVehicle(id);
                            System.out.println("Автомобиль удалён");
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Управляет операциями с записями о парковке.
     */
    private static void manageParkingRecords() {
        while (true) {
            displaySubMenu("Записи о парковке");
            int choice = getIntInput("Выберите опцию: ");
            if (choice == 6) break;
            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: неверная опция!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        long spaceId = getLongInput("Введите ID парковочного места: ");
                        ParkingSpace space = parkingSpaceService.getParkingSpace(spaceId);
                        long vehicleId = getLongInput("Введите ID автомобиля: ");
                        Vehicle vehicle = vehicleService.getVehicle(vehicleId);
                        long clientId = getLongInput("Введите ID клиента: ");
                        Client client = clientService.getClient(clientId);
                        LocalDateTime entryTime = getDateTimeInput("Введите время въезда (гггг-мм-дд чч:мм:сс): ");
                        if (entryTime == null) continue;
                        ParkingRecord record = new ParkingRecord(null, space, vehicle, client, entryTime, null);
                        parkingRecordService.createParkingRecord(record);
                        System.out.println("Запись создана с ID: " + record.getId());
                    }
                    case 2 -> {
                        long id = getLongInput("Введите ID записи: ");
                        ParkingRecord record = parkingRecordService.getParkingRecord(id);
                        System.out.println("\nЗапись о парковке:");
                        System.out.println("ID: " + record.getId());
                        System.out.println("ID места: " + record.getParkingSpace().getId());
                        System.out.println("ID автомобиля: " + record.getVehicle().getId());
                        System.out.println("ID клиента: " + record.getClient().getId());
                        System.out.println("Время въезда: " + record.getEntryTime());
                        System.out.println("Время выезда: " + (record.getExitTime() != null ? record.getExitTime() : "N/A"));
                    }
                    case 3 -> {
                        List<ParkingRecord> records = parkingRecordService.getAllParkingRecords();
                        if (records.isEmpty()) {
                            System.out.println("Записи отсутствуют.");
                        } else {
                            System.out.println("\nСписок записей о парковке:");
                            System.out.println("ID | Место | Авто | Клиент | Время въезда        | Время выезда");
                            System.out.println("--|-------|------|--------|---------------------|-------------");
                            for (ParkingRecord record : records) {
                                System.out.printf("%d | %d | %d | %d | %-19s | %-19s%n",
                                        record.getId(),
                                        record.getParkingSpace().getId(),
                                        record.getVehicle().getId(),
                                        record.getClient().getId(),
                                        record.getEntryTime(),
                                        record.getExitTime() != null ? record.getExitTime() : "N/A");
                            }
                        }
                    }
                    case 4 -> {
                        long id = getLongInput("Введите ID записи: ");
                        long spaceId = getLongInput("Введите новый ID места: ");
                        ParkingSpace space = parkingSpaceService.getParkingSpace(spaceId);
                        long vehicleId = getLongInput("Введите новый ID автомобиля: ");
                        Vehicle vehicle = vehicleService.getVehicle(vehicleId);
                        long clientId = getLongInput("Введите новый ID клиента: ");
                        Client client = clientService.getClient(clientId);
                        LocalDateTime entryTime = getDateTimeInput("Введите новое время въезда (гггг-мм-дд чч:мм:сс): ");
                        if (entryTime == null) continue;
                        LocalDateTime exitTime = null;
                        String exitTimeStr = getStringInput("Введите новое время выезда (гггг-мм-дд чч:мм:сс, или пусто): ", true);
                        if (!exitTimeStr.isEmpty()) {
                            try {
                                exitTime = LocalDateTime.parse(exitTimeStr.replace(" ", "T"));
                            } catch (DateTimeParseException e) {
                                System.out.println("Ошибка: неверный формат времени, будет null");
                            }
                        }
                        ParkingRecord record = new ParkingRecord(id, space, vehicle, client, entryTime, exitTime);
                        parkingRecordService.updateParkingRecord(record);
                        System.out.println("Запись обновлена");
                    }
                    case 5 -> {
                        long id = getLongInput("Введите ID записи: ");
                        String confirm = getStringInput("Подтвердите удаление (да/нет): ", false);
                        if (confirm.equalsIgnoreCase("да")) {
                            parkingRecordService.deleteParkingRecord(id);
                            System.out.println("Запись удалена");
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Отображает подменю для управления сущностями.
     *
     * @param entityName название сущности
     */
    private static void displaySubMenu(String entityName) {
        System.out.println("\n=== " + entityName + " ===");
        System.out.println("1. Создать");
        System.out.println("2. Просмотреть");
        System.out.println("3. Список");
        System.out.println("4. Обновить");
        System.out.println("5. Удалить");
        System.out.println("6. Назад");
    }
}