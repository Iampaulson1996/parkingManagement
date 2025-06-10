package com.parkingManagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Тестовый сервлет для проверки деплоя приложения.
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    /**
     * Обрабатывает GET-запросы и возвращает тестовую страницу.
     *
     * @param req HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если ошибка сервлета
     * @throws IOException если ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Test Servlet Works!</h1>");
        out.println("<p>Application is deployed and reachable.</p>");
    }
}