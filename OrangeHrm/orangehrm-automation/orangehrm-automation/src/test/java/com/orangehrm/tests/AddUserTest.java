package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.AdminPage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AddUserTest extends BaseTest {

    private AdminPage adminPage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToAdmin() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.loginAs(
                ConfigReader.get("validUsername"),
                ConfigReader.get("validPassword"));
        adminPage = dashboard.goToAdmin();
    }

    /** TC_ADMIN_01: Creating a new ESS user with a valid, existing employee name
     *  should succeed and show a confirmation toast. */
    @Test(priority = 1)
    public void testAddSystemUser() {
        adminPage.clickAddUser();
        adminPage.selectUserRole("ESS");
        adminPage.typeEmployeeNameAndPickFirstMatch("a");
        adminPage.enterUsername("autoqa_user_" + System.currentTimeMillis());
        adminPage.enterPassword("AutoQA@1234");
        adminPage.clickSave();

        Assert.assertTrue(adminPage.isSuccessToastDisplayed(), "Success toast not shown after adding a system user");
    }

    /** TC_ADMIN_02: The Admin > User Management list should contain at least one row
     *  after a user has been created. */
    @Test(priority = 2)
    public void testUserListIsNotEmpty() {
        Assert.assertTrue(adminPage.getUserRowCount() > 0, "User Management list is unexpectedly empty");
    }
}
