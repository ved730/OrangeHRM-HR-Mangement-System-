package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LeavePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApplyLeaveTest extends BaseTest {

    private LeavePage leavePage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToLeave() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.loginAs(
                ConfigReader.get("validUsername"),
                ConfigReader.get("validPassword"));
        leavePage = dashboard.goToLeave();
    }

    /** TC_LEAVE_01: Applying for leave with a valid leave type and a future date
     *  should succeed and show a confirmation toast. */
    @Test(priority = 1)
    public void testApplyLeaveWithValidData() {
        leavePage.openApplyLeaveForm();
        leavePage.selectLeaveType("CAN - Bereavement");

        String futureDate = LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        leavePage.enterFromDate(futureDate);

        leavePage.clickApply();
        Assert.assertTrue(leavePage.isLeaveAppliedSuccessfully(),
                "Success toast not shown after applying for leave");
    }
}
