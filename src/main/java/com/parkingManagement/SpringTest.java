package com.parkingManagement;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            System.out.println("Spring контекст загружен!");
            context.close();
        } catch (Exception e) {
            System.err.println("Ошибка Spring: " + e.getMessage());
            e.printStackTrace();
        }
    }
}