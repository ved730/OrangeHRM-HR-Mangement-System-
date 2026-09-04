package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    /** TC_LOGIN_01: Valid credentials should land the user on the Dashboard. */
    @Test(priority = 1)
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.loginAs(
                ConfigReader.get("validUsername"),
                ConfigReader.get("validPassword"));

        Assert.assertTrue(dashboard.isDashboardDisplayed(), "Dashboard header not visible after valid login");
    }

    /** TC_LOGIN_02: Invalid credentials should show the "Invalid credentials" error banner. */
    @Test(priority = 2)
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("InvalidUser");
        loginPage.enterPassword("WrongPassword123");
        loginPage.clickLogin();

        String errorText = loginPage.getInvalidCredentialsError();
        Assert.assertTrue(errorText.contains("Invalid credentials"),
                "Expected 'Invalid credentials' message, got: " + errorText);
    }

    /** TC_LOGIN_03: Submitting with both fields empty should trigger required-field validation. */
    @Test(priority = 3)
    public void testEmptyCredentialsValidation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isRequiredFieldErrorDisplayed(),
                "Required field validation message not displayed for empty login form");
    }

    /** TC_LOGIN_04: A logged-in user can log out and is returned to the login screen. */
    @Test(priority = 4)
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.loginAs(
                ConfigReader.get("validUsername"),
                ConfigReader.get("validPassword"));
        Assert.assertTrue(dashboard.isDashboardDisplayed());

        LoginPage loginPageAfterLogout = dashboard.logout();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"),
                "URL does not indicate the user was returned to the login page after logout");
    }
}
