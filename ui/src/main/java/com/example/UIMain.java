package com.example;

import com.example.ui.config.UISpringConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class UIMain implements WebApplicationInitializer {

    /**
     * The {@code UIMain} class is a Web Application Initializer that configures
     * and initializes the Spring MVC DispatcherServlet programmatically.
     *
     * <p>It replaces the traditional {@code web.xml} configuration by setting up
     * an {@link AnnotationConfigWebApplicationContext} and registering the
     * {@link UISpringConfig} class for application context configuration.</p>
     *
     * <p>This class implements the {@link WebApplicationInitializer} interface,
     * which is automatically detected by Spring Servlet 3.0+ containers.</p>
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(UISpringConfig.class);

        // Create and register the DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}