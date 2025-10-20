package com.saucedemo.hooks;

import com.saucedemo.utils.Setup;
import com.saucedemo.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        WebDriver driver = Setup.getDriver();
        System.out.println("HOOKS: Scenario démarré, WebDriver prêt → " + driver);
    }

    @After
    public void afterScenario(Scenario scenario) {
        WebDriver driver = Setup.getDriver();
        System.out.println("HOOKS: Scenario terminé, nettoyage WebDriver");

        // 📸 Capture uniquement si le scénario a échoué
        if (scenario.isFailed()) {
            System.out.println("⚠️ Le scénario a échoué : capture d’écran...");
            String screenshotPath = ScreenshotUtils.takeScreenshot(driver, scenario.getName());
            
            if (screenshotPath != null) {
                try {
                    // 👉 Attachement de la capture au rapport Cucumber/Allure
                    scenario.attach(
                            java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(screenshotPath)),
                            "image/png",
                            "Capture d’écran en cas d’échec"
                    );
                } catch (Exception e) {
                    System.err.println("❌ Impossible d'attacher la capture : " + e.getMessage());
                }
            }
        }

        Setup.tearDown();
    }
}
