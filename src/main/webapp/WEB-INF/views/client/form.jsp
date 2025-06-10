<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${client.id != null ? 'Редактирование клиента' : 'Создание клиента'}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f9; }
        h1 { color: #333; }
        form { background-color: #fff; padding: 20px; border-radius: 5px; max-width: 500px; }
        label { display: block; margin-bottom: 10px; }
        input[type="text"], input[type="email"] { width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ddd; }
        button { background-color: #0066cc; color: #fff; border: none; padding: 10px 20px; cursor: pointer; }
        button:hover { background-color: #0052a3; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>${client.id != null ? 'Редактирование клиента' : 'Создание клиента'}</h1>
<form action="${client.id != null ? '/parking-management/clients/' + client.id : '/parking-management/clients'}" method="post">
    <label>Имя: <input type="text" name="name" value="${client.name}" required></label>
    <label>Телефон: <input type="text" name="phone" value="${client.phone}"></label>
    <label>Email: <input type="email" name="email" value="${client.email}"></label>
    <button type="submit">Сохранить</button>
</form>
<a href="/parking-management/clients">Назад</a>
</body>
</html>