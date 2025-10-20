package com.saucedemo.base;

import org.openqa.selenium.WebDriver;
import com.saucedemo.utils.SeleniumUtils;
import com.saucedemo.utils.Wait;

public abstract class BasePage {

    protected WebDriver driver;
    protected SeleniumUtils seleniumUtils; 
    protected Wait wait;

    public BasePage(WebDriver driver, SeleniumUtils seleniumUtils, Wait wait) {
        this.driver = driver;
        this.seleniumUtils = seleniumUtils; 
        this.wait = wait;
    }
}
