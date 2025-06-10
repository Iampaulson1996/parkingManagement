<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${parkingSpace.id != null ? 'Редактирование парковочного места' : 'Создание парковочного места'}</title>
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
<h1>${parkingSpace.id != null ? 'Редактирование парковочного места' : 'Создание парковочного места'}</h1>
<form action="${parkingSpace.id != null ? '/parking-management/parkingSpaces/' + parkingSpace.id : '/parking-management/parkingSpaces'}" method="post">
    <label>Парковка:
        <select name="parkingLotId" required>
            <c:forEach var="lot" items="${parkingLots}">
                <option value="${lot.id}" ${parkingSpace.parkingLot != null && parkingSpace.parkingLot.id == lot.id ? 'selected' : ''}>${lot.name}</option>
            </c:forEach>
        </select>
    </label>
    <label>Номер места: <input type="text" name="spaceNumber" value="${parkingSpace.spaceNumber}" required></label>
    <label>Тип: <select name="type" required>
        <option value="REGULAR" ${parkingSpace.type == 'REGULAR' ? 'selected' : ''}>Обычный</option>
        <option value="DISABLED" ${parkingSpace.type == 'DISABLED' ? 'selected' : ''}>Для инвалидов</option>
        <option value="VIP" ${parkingSpace.type == 'VIP' ? 'selected' : ''}>VIP</option>
    </select></label>
    <button type="submit">Сохранить</button>
</form>
<a href="/parking-management/parkingSpaces">Назад</a>
</body>
</html>