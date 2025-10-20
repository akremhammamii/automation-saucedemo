package com.saucedemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 🔧 Wait — Classe utilitaire finale pour toutes les attentes explicites Selenium.
 * Utilisée dans BasePage et les Pages du framework (LoginPage, etc.)
 */
public final class Wait {

    private static final Logger logger = LogManager.getLogger(Wait.class);

    private final WebDriver driver;
    private final Duration defaultTimeout;
    private final Duration pollingInterval;
    private final WebDriverWait cachedWait;

    /** Constructeur par défaut : timeout = 15s, polling = 500ms */
    public Wait(WebDriver driver) {
        this(driver, Duration.ofSeconds(15), Duration.ofMillis(500));
    }

    /** Constructeur personnalisé */
    public Wait(WebDriver driver, Duration defaultTimeout, Duration pollingInterval) {
        this.driver = Objects.requireNonNull(driver, "Driver ne doit pas être null");
        this.defaultTimeout = defaultTimeout;
        this.pollingInterval = pollingInterval;
        this.cachedWait = createWait(defaultTimeout);
    }

    // ===========================================================
    // 🔹 MÉTHODES D’ATTENTE DE BASE
    // ===========================================================

    public WebElement waitForElementToBeVisible(By locator) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(locator),
                defaultTimeout, "Élément non visible : " + locator);
    }

    public WebElement waitForElementToBeClickable(By locator) {
        return waitUntil(ExpectedConditions.elementToBeClickable(locator),
                defaultTimeout, "Élément non cliquable : " + locator);
    }

    public WebElement waitForPresenceOfElement(By locator) {
        return waitUntil(ExpectedConditions.presenceOfElementLocated(locator),
                defaultTimeout, "Élément non présent dans le DOM : " + locator);
    }

    public boolean waitForInvisibility(By locator) {
        return waitUntil(ExpectedConditions.invisibilityOfElementLocated(locator),
                defaultTimeout, "Élément toujours visible : " + locator);
    }

    public List<WebElement> waitForAllElementsVisible(By locator) {
        return waitUntil(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator),
                defaultTimeout, "Tous les éléments ne sont pas visibles : " + locator);
    }

    // ===========================================================
    // 🔹 MÉTHODES D’ATTENTE SPÉCIFIQUES
    // ===========================================================

    public boolean waitForTextToBePresent(By locator, String text) {
        return waitUntil(ExpectedConditions.textToBePresentInElementLocated(locator, text),
                defaultTimeout, "Texte '" + text + "' non trouvé dans : " + locator);
    }

    public boolean waitForExactText(By locator, String text) {
        return waitUntil(ExpectedConditions.textToBe(locator, text),
                defaultTimeout, "Texte exact '" + text + "' non trouvé dans : " + locator);
    }

    public boolean waitForAttributeContains(By locator, String attribute, String value) {
        return waitUntil(ExpectedConditions.attributeContains(locator, attribute, value),
                defaultTimeout, "L’attribut '" + attribute + "' ne contient pas '" + value + "'");
    }

    // ===========================================================
    // 🔹 MÉTHODES SÉCURISÉES (safe)
    // ===========================================================

    public Optional<WebElement> waitForElementVisibleSafe(By locator) {
        return safeWait(() -> waitForElementToBeVisible(locator));
    }

    public Optional<WebElement> waitForElementClickableSafe(By locator) {
        return safeWait(() -> waitForElementToBeClickable(locator));
    }

    private <T> Optional<T> safeWait(SupplierWithException<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (TimeoutException e) {
            logger.warn("⚠️ Timeout dépassé pour attente safe");
            return Optional.empty();
        }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws TimeoutException;
    }

    // ===========================================================
    // 🌐 ATTENTES DE PAGE
    // ===========================================================

    public void waitForPageLoad() {
        logger.debug("🌍 Attente du chargement complet de la page...");
        waitForCondition(driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"),
                defaultTimeout
        );
        logger.info("✅ Page chargée !");
    }

    public void waitForJQueryInactive() {
        if (!isJsLibraryPresent("return typeof jQuery !== 'undefined'")) {
            logger.debug("ℹ️ jQuery non détecté — attente ignorée");
            return;
        }
        waitForCondition(driver ->
                Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript("return jQuery.active === 0;")),
                defaultTimeout
        );
        logger.info("✅ jQuery inactif");
    }

    public void waitForAngularStable() {
        if (!isJsLibraryPresent("return typeof getAllAngularTestabilities !== 'undefined'")) {
            logger.debug("ℹ️ Angular non détecté — attente ignorée");
            return;
        }
        waitForCondition(driver ->
                Boolean.TRUE.equals(((JavascriptExecutor) driver)
                        .executeScript("return window.getAllAngularTestabilities().every(t => t.isStable());")),
                defaultTimeout
        );
        logger.info("✅ Angular stable");
    }

    public void waitForFullAppLoad() {
        logger.debug("🚀 Attente du chargement complet de l'application (Page + JS libs)...");
        waitForPageLoad();
        waitForJQueryInactive();
        waitForAngularStable();
        logger.info("🎉 Application complètement chargée !");
    }

    // ===========================================================
    // 🔹 MÉTHODES GÉNÉRIQUES INTERNES
    // ===========================================================

    private <T> T waitUntil(ExpectedCondition<T> condition, Duration timeout, String message) {
        try {
            return getWait(timeout).until(condition);
        } catch (TimeoutException e) {
            logger.error("❌ Timeout après {}s : {}", timeout.getSeconds(), message);
            throw e;
        }
    }

    public <T> T waitForCondition(Function<WebDriver, T> condition, Duration timeout) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(JavascriptException.class)
                .until(condition);
    }

    public <T> T waitForCondition(Function<WebDriver, T> condition) {
        return waitForCondition(condition, defaultTimeout);
    }

    private WebDriverWait createWait(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.pollingEvery(pollingInterval);
        wait.ignoring(StaleElementReferenceException.class);
        return wait;
    }

    private WebDriverWait getWait(Duration timeout) {
        return timeout.equals(defaultTimeout) ? cachedWait : createWait(timeout);
    }

    private boolean isJsLibraryPresent(String script) {
        try {
            Object result = ((JavascriptExecutor) driver).executeScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }

    // ===========================================================
    // 🔹 GETTERS
    // ===========================================================

    public Duration getDefaultTimeout() {
        return defaultTimeout;
    }

    public Duration getPollingInterval() {
        return pollingInterval;
    }
}
