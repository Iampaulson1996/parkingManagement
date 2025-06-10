<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список записей о парковке</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f9; }
        h1 { color: #333; }
        table { border-collapse: collapse; width: 100%; background-color: #fff; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; color: #333; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
        button { background-color: #cc0000; color: #fff; border: none; padding: 5px 10px; cursor: pointer; }
        button:hover { background-color: #b30000; }
    </style>
</head>
<body>
<h1>Список записей о парковке</h1>
<a href="/parking-management/parkingRecords/new">Добавить запись</a>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Место</th>
        <th>Автомобиль</th>
        <th>Клиент</th>
        <th>Время въезда</th>
        <th>Время выезда</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="record" items="${parkingRecords}">
        <tr>
            <td>${record.id}</td>
            <td>${record.parkingSpace.spaceNumber}</td>
            <td>${record.vehicle.vehicleId}</td>
            <td>${record.clientId}</td>
            <td>${record.entryTime}</td>
            <td>${record.exitTime}</td>
            <td>
                <a href="/parking-management/parkingRecords/${record.id}">Просмотр</a> |
                <a href="/parking-management/parkingRecords/${record.id}/edit">Редактировать</a> |
                <form action="/parking-management/parkingRecords/${record.id}/delete" method="post" style="display:inline;">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<a href="/parking-management">На главную</a>
</body>
</html>