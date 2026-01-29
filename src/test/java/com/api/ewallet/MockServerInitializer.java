package com.api.ewallet;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@Configuration
public class MockServerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    ClientAndServer clientServer;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        int port = PortFactory.findFreePort();

        System.out.println("Mock server port " + port);
        clientServer = startClientAndServer(port);

        System.out.println("Mock server port " + clientServer.getLocalPort());

        TestPropertyValues.of("mock.server.basePath=http://localhost:" + clientServer.getLocalPort())
                .applyTo(applicationContext);

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        beanFactory.registerSingleton("clientServer", clientServer);
    }
}
