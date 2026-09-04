package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private final WebDriver driver;
    private final int wait = ConfigReader.getInt("explicitWait");

    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorAlert = By.cssSelector(".oxd-alert-content-text");
    private final By requiredFieldError = By.cssSelector(".oxd-input-group__message");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        WebElement field = WaitUtils.waitForVisibility(driver, usernameField, wait);
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement field = WaitUtils.waitForVisibility(driver, passwordField, wait);
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        WaitUtils.waitForClickable(driver, loginButton, wait).click();
    }

    public DashboardPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver);
    }

    public String getInvalidCredentialsError() {
        return WaitUtils.waitForVisibility(driver, errorAlert, wait).getText();
    }

    public boolean isRequiredFieldErrorDisplayed() {
        return WaitUtils.waitForVisibility(driver, requiredFieldError, wait).isDisplayed();
    }
}
