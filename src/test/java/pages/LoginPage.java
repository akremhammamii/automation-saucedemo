package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.saucedemo.base.BasePage;
import com.saucedemo.utils.SeleniumUtils;
import com.saucedemo.utils.Wait;

public class LoginPage extends BasePage {

    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver, SeleniumUtils seleniumUtils, Wait wait) {
        super(driver, seleniumUtils, wait);
    }

    // ✅ Saisir le nom d'utilisateur
    public void enterUsername(String username) {
        WebElement usernameInput = wait.waitForElementToBeVisible(USERNAME_FIELD);
        seleniumUtils.clearAndWriteText(usernameInput, username);
    }

    // ✅ Saisir le mot de passe
    public void enterPassword(String password) {
        WebElement passwordInput = wait.waitForElementToBeVisible(PASSWORD_FIELD);
        seleniumUtils.clearAndWriteText(passwordInput, password);
    }

    // ✅ Cliquer sur le bouton de connexion
    public void clickLogin() {
        seleniumUtils.click(wait.waitForElementToBeClickable(LOGIN_BUTTON));
    }

    // ✅ Connexion directe en une étape (facultatif)
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ✅ Vérifie si un message d'erreur est présent
    public boolean isErrorDisplayed() {
        try {
            return wait.waitForElementToBeVisible(ERROR_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Récupère le message d'erreur
    public String getErrorMessage() {
        try {
            return wait.waitForElementToBeVisible(ERROR_MESSAGE).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
