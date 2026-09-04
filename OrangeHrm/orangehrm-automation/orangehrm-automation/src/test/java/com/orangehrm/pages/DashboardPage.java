package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {

    private final WebDriver driver;
    private final int wait = ConfigReader.getInt("explicitWait");

    private final By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private final By userDropdown = By.className("oxd-userdropdown-tab");
    private final By logoutLink = By.xpath("//a[text()='Logout']");
    private final By pimMenuItem = By.xpath("//span[text()='PIM']");
    private final By leaveMenuItem = By.xpath("//span[text()='Leave']");
    private final By adminMenuItem = By.xpath("//span[text()='Admin']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDashboardDisplayed() {
        return WaitUtils.waitForVisibility(driver, dashboardHeader, wait).isDisplayed();
    }

    public PimPage goToPim() {
        WaitUtils.waitForClickable(driver, pimMenuItem, wait).click();
        return new PimPage(driver);
    }

    public LeavePage goToLeave() {
        WaitUtils.waitForClickable(driver, leaveMenuItem, wait).click();
        return new LeavePage(driver);
    }

    public AdminPage goToAdmin() {
        WaitUtils.waitForClickable(driver, adminMenuItem, wait).click();
        return new AdminPage(driver);
    }

    public LoginPage logout() {
        WaitUtils.waitForClickable(driver, userDropdown, wait).click();
        WaitUtils.waitForClickable(driver, logoutLink, wait).click();
        return new LoginPage(driver);
    }
}
