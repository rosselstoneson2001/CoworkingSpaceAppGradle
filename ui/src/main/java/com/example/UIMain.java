package com.example;

import com.example.ui.config.UISpringConfig;
import com.example.ui.impl.GeneralMenuUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UIMain {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(UISpringConfig.class);

        GeneralMenuUI generalMenuUI = context.getBean(GeneralMenuUI.class);
        generalMenuUI.generalMenu();

        context.close();
    }
}