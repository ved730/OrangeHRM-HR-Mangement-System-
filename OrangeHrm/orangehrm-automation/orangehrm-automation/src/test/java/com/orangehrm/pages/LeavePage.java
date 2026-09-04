package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class LeavePage {

    private final WebDriver driver;
    private final int wait = ConfigReader.getInt("explicitWait");

    private final By applyMenuItem = By.xpath("//a[contains(text(),'Apply')]");
    private final By leaveTypeDropdown = By.xpath("//label[text()='Leave Type']/../following-sibling::div//div[contains(@class,'oxd-select-text')]");
    private final By dropdownOptions = By.cssSelector(".oxd-select-dropdown .oxd-select-option");
    private final By fromDateField = By.xpath("(//label[text()='From Date']/../following-sibling::div//input)[1]");
    private final By applyButton = By.xpath("//button[text()=' Apply ']");
    private final By successToast = By.cssSelector(".oxd-toast-content--success");
    private final By loadingSpinner = By.className("oxd-loading-spinner");

    public LeavePage(WebDriver driver) {
        this.driver = driver;
    }

    public void openApplyLeaveForm() {
        WaitUtils.waitForClickable(driver, applyMenuItem, wait).click();
    }

    public void selectLeaveType(String leaveTypeText) {
        WaitUtils.waitForClickable(driver, leaveTypeDropdown, wait).click();
        WaitUtils.waitForVisibility(driver, dropdownOptions, wait);
        driver.findElements(dropdownOptions).stream()
                .filter(o -> o.getText().trim().equalsIgnoreCase(leaveTypeText))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Leave type not found: " + leaveTypeText))
                .click();
    }

    public void enterFromDate(String date) {
        WaitUtils.waitForVisibility(driver, fromDateField, wait).sendKeys(date);
    }

    public void clickApply() {
        WaitUtils.waitForClickable(driver, applyButton, wait).click();
        try {
            WaitUtils.waitForInvisibility(driver, loadingSpinner, wait);
        } catch (Exception ignored) {
            // Spinner may already be gone.
        }
    }

    public boolean isLeaveAppliedSuccessfully() {
        return WaitUtils.waitForVisibility(driver, successToast, wait).isDisplayed();
    }
}
