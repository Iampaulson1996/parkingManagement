<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Просмотр клиента</title>
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
<h1>Просмотр клиента</h1>
<div>
    <p><strong>ID:</strong> ${client.id}</p>
    <p><strong>Имя:</strong> ${client.name}</p>
    <p><strong>Телефон:</strong> ${client.phone}</p>
    <p><strong>Email:</strong> ${client.email}</p>
    <a href="/parking-management/clients/${client.id}/edit">Редактировать</a>
    <form action="/parking-management/clients/${client.id}/delete" method="post" style="display:inline;">
        <button type="submit">Удалить</button>
    </form>
    <a href="/parking-management/clients">Назад</a>
</div>
</body>
</html>