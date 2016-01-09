package com.artronics.sdwn.device.config;

import com.artronics.sdwn.controller.SdwnApplication;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class DeviceWebInitializerConfig extends
                                  AbstractAnnotationConfigDispatcherServletInitializer
{
    private final static Logger log = Logger.getLogger(DeviceWebInitializerConfig.class);

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException
    {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SdwnApplication.class);
        ctx.setServletContext(servletContext);
        ctx.refresh();
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher",
                                                                        new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { SdwnApplication.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { DeviceServletConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
