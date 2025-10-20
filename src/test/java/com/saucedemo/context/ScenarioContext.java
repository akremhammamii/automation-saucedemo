package com.saucedemo.context;

import org.openqa.selenium.WebDriver;
import com.saucedemo.utils.SeleniumUtils;
import com.saucedemo.utils.Wait;
import com.saucedemo.utils.ConfigFileReader;
import com.saucedemo.utils.Setup;
import pages.LoginPage;

public class ScenarioContext {

    private WebDriver driver;
    public SeleniumUtils seleniumUtils;   // ✅ Créé ici, pas injecté
    public Wait wait;                     // ✅ Créé ici, pas injecté
    public LoginPage loginPage;
    public ConfigFileReader configFileReader;

    // ✅ SEUL paramètre : ConfigFileReader — PicoContainer sait le créer
    public ScenarioContext(ConfigFileReader configFileReader) {
        this.driver = Setup.getDriver();                  // Récupère le driver via Setup (ThreadLocal)
        this.configFileReader = configFileReader;

        // ✅ Créez manuellement les dépendances avec le driver disponible
        this.seleniumUtils = new SeleniumUtils(driver);
        this.wait = new Wait(driver);

        // ✅ Créez la page Login avec les objets que vous venez de créer
        this.loginPage = new LoginPage(driver, seleniumUtils, wait);
    }

    public WebDriver getDriver() {
        return driver;
    }
}