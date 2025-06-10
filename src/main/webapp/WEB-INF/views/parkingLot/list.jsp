<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список парковок</title>
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
<h1>Список парковок</h1>
<a href="/parking-management/parkingLots/new">Добавить парковку</a>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Адрес</th>
        <th>Вместимость</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="parkingLot" items="${parkingLots}">
        <tr>
            <td>${parkingLot.id}</td>
            <td>${parkingLot.name}</td>
            <td>${parkingLot.address}</td>
            <td>${parkingLot.capacity}</td>
            <td>
                <a href="/parking-management/parkingLots/${parkingLot.id}">Просмотр</a> |
                <a href="/parking-management/parkingLots/${parkingLot.id}/edit">Редактировать</a> |
                <form action="/parking-management/parkingLots/${parkingLot.id}/delete" method="post" style="display:inline;">
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