<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${vehicle.id != null ? 'Редактирование автомобиля' : 'Создание автомобиля'}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f9; }
        h1 { color: #333; }
        form { background-color: #fff; padding: 20px; border-radius: 5px; max-width: 500px; }
        label { display: block; margin-bottom: 10px; }
        input[type="text"], select { width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ddd; }
        button { background-color: #0066cc; color: #fff; border: none; padding: 10px 20px; cursor: pointer; }
        button:hover { background-color: #0052a3; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>${vehicle.id != null ? 'Редактирование автомобиля' : 'Создание автомобиля'}</h1>
<form action="${vehicle.id != null ? '/parking-management/vehicles/' + vehicle.id : '/parking-management/vehicles'}" method="post">
    <label>Клиент:
        <select name="clientId" required>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}" ${vehicle.client != null && vehicle.client.id == client.id ? 'selected' : ''}>${client.name}</option>
            </c:forEach>
        </select>
    </label>
    <label>Регистрационный номер: <input type="text" name="licensePlate" value="${vehicle.licensePlate}" required></label>
    <label>Марка: <input type="text" name="brand" value="${vehicle.brand}"></label>
    <label>Модель: <input type="text" name="model" value="${vehicle.model}"></label>
    <button type="submit">Сохранить</button>
</form>
<a href="/parking-management/vehicles">Назад</a>
</body>
</html>