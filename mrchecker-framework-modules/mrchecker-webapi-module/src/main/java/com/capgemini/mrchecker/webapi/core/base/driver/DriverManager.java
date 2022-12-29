package com.capgemini.mrchecker.webapi.core.base.driver;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.webapi.core.base.runtime.RuntimeParameters;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.FatalStartupException;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.opentable.extension.BodyTransformer;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class DriverManager {
    private static final ThreadLocal<VirtualizedService> driverVirtualizedService = new ThreadLocal<>();
    private static PropertiesFileSettings propertiesFileSettings;

    @Inject
    public DriverManager(@Named("properties") PropertiesFileSettings propertiesFileSettings) {
        if (Objects.isNull(DriverManager.propertiesFileSettings)) {
            DriverManager.propertiesFileSettings = propertiesFileSettings;
        }
    }

    public void start() {
        if (DriverManager.propertiesFileSettings.isVirtualServerEnabled()) {
            DriverManager.getDriverVirtualService();
        }
        DriverManager.getDriverWebAPI();
    }

    public void stop() {
        try {
            closeDriverVirtualServer();
            BFLogger.logDebug("Closing Driver in stop()");
        } catch (Exception e) {
            // TODO: refactor that
            e.printStackTrace();
        }
    }

    // TODO: refactor that
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stop();
    }

    public static void clearAllDrivers() {
        driverVirtualizedService.remove();
    }

    public static WireMock getDriverVirtualService() {
        VirtualizedService virtualizedService = getVirtualizedService();
        WireMock driver = virtualizedService.getDriver();
        BFLogger.logDebug("Driver for server: " + driver.toString());
        return driver;
    }

    public static WireMockServer getDriverVirtualServerService() {
        VirtualizedService virtualizedService = getVirtualizedService();
        WireMockServer driver = virtualizedService.getDriverServer();
        BFLogger.logDebug("Driver for virtual server: " + driver.toString());
        return driver;
    }

    public static int getHttpPort() {
        VirtualizedService virtualizedService = getVirtualizedService();
        return virtualizedService.getHttpPort();
    }

    public static String getHttpHost() {
        VirtualizedService virtualizedService = getVirtualizedService();
        return virtualizedService.getHttpHost();
    }

    public static String getEndpointBaseUri() {
        VirtualizedService virtualizedService = getVirtualizedService();
        return virtualizedService.getEndpointBaseUri();
    }

    private static VirtualizedService getVirtualizedService() {
        VirtualizedService virtualizedService = driverVirtualizedService.get();
        if (Objects.isNull(virtualizedService)) {
            virtualizedService = createDriverVirtualServer();
            driverVirtualizedService.set(virtualizedService);
        }
        return virtualizedService;
    }

    public static void closeDriverVirtualServer() {
        VirtualizedService virtualizedService = driverVirtualizedService.get();

        if (!Objects.isNull(virtualizedService)) {
            WireMock driver = virtualizedService.getDriver();
            WireMockServer driverServer = virtualizedService.getDriverServer();
            BFLogger.logDebug(
                    "Closing communication to Virualize Service under: " + driver.toString());

            try {
                if (!Objects.isNull(driver)) {
                    // driver.shutdown();
                }

                if (!Objects.isNull(driverServer)) {
                    driverServer.stop();
                }

            } catch (Exception e) {
                BFLogger.logDebug("Ooops! Something went wrong while closing the driver");
            } finally {
                clearAllDrivers();
            }
        }
    }

    static VirtualizedService createDriverVirtualServer() {
        BFLogger.logDebug("Creating new Mock Server");

        VirtualizedService virtualizedService = Driver.WIREMOCK.getDriver();

        BFLogger.logDebug("Running: " + virtualizedService.toString());
        return virtualizedService;
    }

    private enum Driver {
        WIREMOCK {
            public VirtualizedService getDriver() throws FatalStartupException {

                WireMock driver;
                WireMockServerMrChecker driverServer = null;

                int httpPort = getPort();
                String httpHost = getHost();

                if ("".equals(httpHost) || "http://localhost".equals(httpHost) || "https://localhost".equals(httpHost)) {
                    WireMockConfiguration wireMockConfig = wireMockConfig().extensions(new BodyTransformer());

                    wireMockConfig.port(httpPort);
                    driverServer = new WireMockServerMrChecker(wireMockConfig);

                    try {
                        driverServer.start();
                    } catch (FatalStartupException e) {
                        BFLogger.logError(e.getMessage() + "host " + httpHost + ":" + httpPort);
                        throw new FatalStartupException(e);
                    }
                    driver = driverServer.getClient();
                } else {
                    driver = new WireMock(httpHost, httpPort);
                    driver.startStubRecording("http://example.mocklab.io");
                    driver.stopStubRecording();
                }
                return new VirtualizedService(driver, driverServer, httpHost, httpPort);

            }

            private String getHost() {
                return RuntimeParameters.MOCK_HTTP_HOST.getValue();
            }

            private int getPort() {
                return RuntimeParameters.MOCK_HTTP_PORT.getValue()
                        .isEmpty()
                        ? findFreePort()
                        : getInteger(RuntimeParameters.MOCK_HTTP_PORT.getValue());
            }

            /**
             * Returns a free port number on localhost.
             * Heavily inspired from org.eclipse.jdt.launching.SocketUtil (to avoid a dependency to JDT just because of
             * this).
             * Slightly improved with close() missing in JDT. And throws exception instead of returning -1.
             *
             * @return a free port number on localhost
             * @throws IllegalStateException
             *             if unable to find a free port
             */
            private int findFreePort() throws IllegalStateException {
                try (ServerSocket socket = new ServerSocket(0)) {
                    socket.setReuseAddress(true);
                    int port = socket.getLocalPort();
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // Ignore IOException on close()
                    }
                    return port;
                } catch (IOException e) {
                    // TODO: refactor that
                }
                throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
            }

            private int getInteger(String value) {
                int number = 0;
                try {
                    number = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    BFLogger.logError("Unable convert to integer value=" + value + " Setting default value=0");
                }
                return number;
            }
        };

        public VirtualizedService getDriver() {
            return null;
        }
    }

    public static RequestSpecification getDriverWebAPI() {
        RestAssuredConfig config = new RestAssuredConfig().encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
        return getDriverWebAPI(config);
    }

    public static RequestSpecification getDriverWebAPI(RestAssuredConfig config) {
        RequestSpecification driver = createDriverWebAPI(config);
        BFLogger.logDebug("driver:" + driver.toString());
        return driver;
    }

    /**
     * Method sets desired 'driver' depends on chosen parameters
     */
    private static RequestSpecification createDriverWebAPI(RestAssuredConfig config) {
        BFLogger.logDebug("Creating new driver.");
        RestAssured.config = config;
        return given();
    }
}