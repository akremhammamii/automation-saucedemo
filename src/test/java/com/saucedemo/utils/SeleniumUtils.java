package com.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumUtils {
    
    private final WebDriver driver;
    
    public SeleniumUtils(WebDriver driver) {
        this.driver = driver;
    }
    
    public void navigateTo(String url) {
        driver.get(url);
    }
    
    public void clearAndWriteText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }
    
    public void writeText(WebElement element, String text) {
        element.sendKeys(text);
    }
    
    public void click(WebElement element) {
        element.click();
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
}