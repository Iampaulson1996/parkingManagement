<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Просмотр записи о парковке</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f9; }
        h1 { color: #333; }
        div { background-color: #fff; padding: 20px; border-radius: 5px; max-width: 500px; }
        p { margin: 10px 0; }
        a { color: #0066cc; text-decoration: none; margin-right: 10px; }
        a:hover { text-decoration: underline; }
        button { background-color: #cc0000; color: #fff; border: none; padding: 5px 10px; cursor: pointer; }
        button:hover { background-color: #b30000; }
    </style>
</head>
<body>
<h1>Просмотр записи о парковке</h1>
<div>
    <p><strong>ID:</strong> ${parkingRecord.id}</p>
    <p><strong>Место:</strong> ${parkingRecord.parkingSpace.spaceNumber}</p>
    <p><strong>Автомобиль:</strong> ${parkingRecord.vehicle.licensePlate}</p>
    <p><strong>Клиент:</strong> ${parkingRecord.client.name}</p>
    <p><strong>Время въезда:</strong> ${parkingRecord.entryTime}</p>
    <p><strong>Время выезда:</strong> ${parkingRecord.exitTime}</p>
    <a href="/parking-management/parkingRecords/${parkingRecord.id}/edit">Редактировать</a>
    <form action="/parking-management/parkingRecords/${parkingRecord.id}/delete" method="post" style="display:inline;">
        <button type="submit">Удалить</button>
    </form>
    <a href="/parking-management/parkingRecords">Назад</a>
</div>
</body>
</html>