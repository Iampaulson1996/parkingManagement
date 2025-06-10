<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${parkingLot.id != null ? 'Редактирование парковки' : 'Создание парковки'}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f9; }
        h1 { color: #333; }
        form { background-color: #fff; padding: 20px; border-radius: 5px; max-width: 500px; }
        label { display: block; margin-bottom: 10px; }
        input[type="text"], input[type="number"] { width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ddd; }
        button { background-color: #0066cc; color: #fff; border: none; padding: 10px 20px; cursor: pointer; }
        button:hover { background-color: #0052a3; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>${parkingLot.id != null ? 'Редактирование парковки' : 'Создание парковки'}</h1>
<form action="${parkingLot.id != null ? '/parking-management/parkingLots/' + parkingLot.id : '/parking-management/parkingLots'}" method="post">
    <label>Название: <input type="text" name="name" value="${parkingLot.name}" required></label>
    <label>Адрес: <input type="text" name="address" value="${parkingLot.address}" required></label>
    <label>Вместимость: <input type="number" name="capacity" value="${parkingLot.capacity}" min="1" required></label>
    <button type="submit">Сохранить</button>
</form>
<a href="/parking-management/parkingLots">Назад</a>
</body>
</html>