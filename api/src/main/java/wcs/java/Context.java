package wcs.java;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

public class Context implements wcs.core.Context {

    GenericApplicationContext context;

     public void initContext(ClassLoader cl) {
        context = new GenericApplicationContext();
        context.setClassLoader(cl);
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
        xmlReader.loadBeanDefinitions(new ClassPathResource("agilesites_context.xml",cl));
        context.refresh();
    }

    public Object getBean(String classname) {
        String beanName = null;
        try {
            beanName = Class.forName(classname).getSimpleName();
            beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return context.getBean(beanName);
    }
}
