package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage {

    private final WebDriver driver;
    private final int wait = ConfigReader.getInt("explicitWait");

    private final By addButton = By.xpath("//button[text()=' Add ']");
    private final By userRoleDropdown = By.xpath("(//div[contains(@class,'oxd-select-text')])[1]");
    private final By dropdownOptions = By.cssSelector(".oxd-select-dropdown .oxd-select-option");
    private final By employeeNameField = By.xpath("//label[text()='Employee Name']/../following-sibling::div//input");
    private final By employeeNameAutocompleteOption = By.cssSelector(".oxd-autocomplete-dropdown-option");
    private final By usernameField = By.xpath("//label[text()='Username']/../following-sibling::div//input");
    private final By passwordField = By.xpath("(//label[text()='Password']/../following-sibling::div//input)[1]");
    private final By confirmPasswordField = By.xpath("(//label[text()='Confirm Password']/../following-sibling::div//input)[1]");
    private final By saveButton = By.xpath("//button[@type='submit']");
    private final By successToast = By.cssSelector(".oxd-toast-content--success");
    private final By userTableRows = By.cssSelector(".oxd-table-card");
    private final By deleteIconOnRow = By.xpath(".//button[@title='Delete']");
    private final By deleteConfirmButton = By.xpath("//button[text()=' Yes, Delete ']");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAddUser() {
        WaitUtils.waitForClickable(driver, addButton, wait).click();
    }

    public void selectUserRole(String role) {
        WaitUtils.waitForClickable(driver, userRoleDropdown, wait).click();
        driver.findElements(dropdownOptions).stream()
                .filter(o -> o.getText().trim().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Role not found: " + role))
                .click();
    }

    public void typeEmployeeNameAndPickFirstMatch(String partialName) {
        WaitUtils.waitForVisibility(driver, employeeNameField, wait).sendKeys(partialName);
        WaitUtils.waitForVisibility(driver, employeeNameAutocompleteOption, wait).click();
    }

    public void enterUsername(String username) {
        WaitUtils.waitForVisibility(driver, usernameField, wait).sendKeys(username);
    }

    public void enterPassword(String password) {
        WaitUtils.waitForVisibility(driver, passwordField, wait).sendKeys(password);
        WaitUtils.waitForVisibility(driver, confirmPasswordField, wait).sendKeys(password);
    }

    public void clickSave() {
        WaitUtils.waitForClickable(driver, saveButton, wait).click();
    }

    public boolean isSuccessToastDisplayed() {
        return WaitUtils.waitForVisibility(driver, successToast, wait).isDisplayed();
    }

    public int getUserRowCount() {
        return driver.findElements(userTableRows).size();
    }

    public void deleteFirstUser() {
        driver.findElements(userTableRows).get(0).findElement(deleteIconOnRow).click();
        WaitUtils.waitForClickable(driver, deleteConfirmButton, wait).click();
    }
}
