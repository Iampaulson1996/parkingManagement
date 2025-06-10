<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список парковочных мест</title>
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
<h1>Список парковочных мест</h1>
<a href="/parking-management/parkingSpaces/new">Добавить парковочное место</a>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Парковка</th>
        <th>Номер места</th>
        <th>Тип</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="space" items="${parkingSpaces}">
        <tr>
            <td>${space.id}</td>
            <td>${space.parkingLot.name}</td>
            <td>${space.spaceNumber}</td>
            <td>${space.type}</td>
            <td>
                <a href="/parking-management/parkingSpaces/${space.id}">Просмотр</a> |
                <a href="/parking-management/parkingSpaces/${space.id}/edit">Редактировать</a> |
                <form action="/parking-management/parkingSpaces/${space.id}/delete" method="post" style="display:inline;">
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