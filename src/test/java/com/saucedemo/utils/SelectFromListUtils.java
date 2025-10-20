package com.saucedemo.utils;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class SelectFromListUtils {
	
	   WebDriver driver;

	    public SelectFromListUtils(WebDriver driver) {
	        this.driver = driver;
	    }

    public void selectByVisibleText(WebElement selectElement, String text) {
        Select select = new Select(selectElement);
        select.selectByVisibleText(text);
    }
    
    public void selectByIndex(WebElement selectElement, int index) {
        Select select = new Select(selectElement);
        select.selectByIndex(index);
    }

    public String getSelectedOptionText(WebElement selectElement) {
        Select select = new Select(selectElement);
        return select.getFirstSelectedOption().getText();
    }

    public List<String> getAllOptions(WebElement selectElement) {
        Select select = new Select(selectElement);
        return select.getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}