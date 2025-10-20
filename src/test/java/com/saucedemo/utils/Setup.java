package com.saucedemo.utils;



import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;



import org.openqa.selenium.WebDriver;


public class Setup {

    // Un ThreadLocal par thread (par scénario si on exécute en parallèle)
    private static final ThreadLocal<WebDriver> THREAD_DRIVER = new ThreadLocal<>();

    private static WebDriver initializeDriver() {
        String browserName = new ConfigFileReader().getProperties("browser");
        boolean isHeadless = Boolean.parseBoolean(new ConfigFileReader().getProperties("headless"));

        System.out.printf("DEBUG: Initializing %s (headless=%b)%n", browserName, isHeadless);

        WebDriver driver;
        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions co = new ChromeOptions();
                if (isHeadless) co.addArguments("--headless=new");
                driver = new ChromeDriver(co);
                break;
            case "edge":
                EdgeOptions eo = new EdgeOptions();
                if (isHeadless) eo.addArguments("--headless=new");
                driver = new EdgeDriver(eo);
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }

        driver.manage().window().maximize();
        System.out.println("DEBUG: Driver initialized successfully.");
        return driver;
    }

    /**
     * Retourne l'instance de WebDriver associée au thread courant,
     * ou l'initialise si nécessaire.
     */
    public static WebDriver getDriver() {
        if (THREAD_DRIVER.get() == null) {
            THREAD_DRIVER.set(initializeDriver());
            System.out.println("DEBUG: New WebDriver instance set for current thread.");
        }
        return THREAD_DRIVER.get();
    }

    /**
     * Ferme et enlève l'instance de WebDriver du ThreadLocal.
     * À appeler après chaque scénario.
     */
    public static void tearDown() {
        WebDriver driver = THREAD_DRIVER.get();
        if (driver != null) {
            try {
                System.out.println("DEBUG: Quitting WebDriver for current thread.");
                driver.quit();
            } catch (Exception e) {
                System.err.println("ERREUR: Impossible de quitter le browser: " + e.getMessage());
            }
            THREAD_DRIVER.remove();
            System.out.println("DEBUG: WebDriver removed from ThreadLocal.");
        } else {
            System.out.println("DEBUG: No WebDriver to quit for current thread.");
        }
    }
}