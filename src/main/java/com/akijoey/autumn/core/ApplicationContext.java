package com.akijoey.autumn.core;

import com.akijoey.autumn.annotation.boot.ComponentScan;
import com.akijoey.autumn.common.Banner;
import com.akijoey.autumn.core.aop.factory.InterceptorFactory;
import com.akijoey.autumn.core.boot.ApplicationRunner;
import com.akijoey.autumn.core.config.Configuration;
import com.akijoey.autumn.core.config.ConfigurationManager;
import com.akijoey.autumn.core.ioc.BeanFactory;
import com.akijoey.autumn.core.ioc.DependencyInjection;
import com.akijoey.autumn.core.mvc.factory.RouteMethodMapper;
import com.akijoey.autumn.factory.ClassFactory;
import com.akijoey.autumn.server.HttpServer;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public final class ApplicationContext {

    private static final ApplicationContext APPLICATION_CONTEXT = new ApplicationContext();

    public void run(Class<?> applicationClass) {
        //print banner
        Banner.print();
        //analyse package
        String[] packageNames = getPackageNames(applicationClass);
        // Load classes with custom annotation
        ClassFactory.loadClass(packageNames);
        // Load routes
        RouteMethodMapper.loadRoutes();
        // Load beans managed by the ioc container
        BeanFactory.loadBeans();
        //load configuration
        loadResources(applicationClass);
        // Load interceptors
        InterceptorFactory.loadInterceptors(packageNames);
        // Traverse all the beans in the ioc container and inject instances for all @Autowired annotated attributes.
        DependencyInjection.inject(packageNames);
        // Applies bean post processors on the classes which are from ClassFactory.
        // For example, the class annotated by @Component or @RestController.
        BeanFactory.applyBeanPostProcessors();
        // Perform some callback events
        callRunners();
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    private static String[] getPackageNames(Class<?> applicationClass) {
        ComponentScan componentScan = applicationClass.getAnnotation(ComponentScan.class);
        return !Objects.isNull(componentScan) ? componentScan.value()
                : new String[]{applicationClass.getPackage().getName()};
    }

    private void callRunners() {
        List<ApplicationRunner> runners = new ArrayList<>(BeanFactory.getBeansOfType(ApplicationRunner.class).values());
        //The last step is to start web application
        runners.add(() -> {
            HttpServer httpServer = new HttpServer();
            httpServer.start();
        });
        for (Object runner : new LinkedHashSet<>(runners)) {
            ((ApplicationRunner) runner).run();
        }
    }

    private void loadResources(Class<?> applicationClass) {
        ClassLoader classLoader = applicationClass.getClassLoader();
        List<Path> filePaths = new ArrayList<>();
        for (String configName : Configuration.DEFAULT_CONFIG_NAMES) {
            URL url = classLoader.getResource(configName);
            if (!Objects.isNull(url)) {
                try {
                    filePaths.add(Paths.get(url.toURI()));
                } catch (URISyntaxException ignored) {
                }
            }
        }
        ConfigurationManager configurationManager = BeanFactory.getBean(ConfigurationManager.class);
        configurationManager.loadResources(filePaths);
    }

}
