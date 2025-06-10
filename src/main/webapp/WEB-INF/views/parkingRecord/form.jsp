<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${parkingRecord.id != null ? 'Редактирование записи' : 'Создание записи'}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f9; }
        h1 { color: #333; }
        form { background-color: #fff; padding: 20px; border-radius: 5px; max-width: 500px; }
        label { display: block; margin-bottom: 10px; }
        input[type="text"], input[type="datetime-local"], select { width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ddd; }
        button { background-color: #0066cc; color: #fff; border: none; padding: 10px 20px; cursor: pointer; }
        button:hover { background-color: #0052a3; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>${parkingRecord.id != null ? 'Редактирование записи' : 'Создание записи'}</h1>
<form action="${parkingRecord.id != null ? '/parking-management/parkingRecords/' + parkingRecord.id : '/parking-management/parkingRecords'}" method="post">
    <label>Парковочное место:
        <select name="parkingSpaceId" required>
            <c:forEach var="space" items="${parkingSpaces}">
                <option value="${space.id}" ${parkingRecord.parkingSpace != null && parkingRecord.parkingSpace.id == space.id ? 'selected' : ''}>${space.spaceNumber}</option>
            </c:forEach>
        </select>
    </label>
    <label>Автомобиль:
        <select name="vehicleId" required>
            <c:forEach var="vehicle" items="${vehicles}">
                <option value="${vehicle.id}" ${parkingRecord.vehicle != null && parkingRecord.vehicle.id == vehicle.id ? 'selected' : ''}>${vehicle.vehicleId}</option>
            </c:forEach>
        </select>
    </label>
    <label>Клиент:
        <select name="clientId" required>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}" ${client != null && parkingRecord.client.id == client.id ? 'selected' : ''}>${client.name}</option>
            </c:forEach>
        </select>
    </label>
    <label>Время въезда: <input type="datetime-local" name="entryTime" value="${parkingRecord.entryTime != null ? parkingRecord.entryTime.toString().substring(0, 16) : ''}" required></label>
    <label>Время выезда: <input type="datetime-local" name="exitTime" value="${parkingRecord.exitTime != null ? parkingRecord.exitTime.toString().substring(0, 16) : ''}"></label>
    <button type="submit">Сохранить</button>
</form>
<a href="/parking-management/parkingRecords">Назад</a>
</body>
</html>