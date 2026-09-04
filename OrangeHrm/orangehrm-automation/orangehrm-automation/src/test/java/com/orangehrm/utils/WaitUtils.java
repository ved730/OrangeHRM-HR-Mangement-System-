package com.orangehrm.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

/**
 * Central place for every wait / alert / frame / window handling strategy used
 * across the framework. Keeping this here means no test or page class ever
 * calls Thread.sleep() directly.
 */
public class WaitUtils {

    private WaitUtils() {
    }

    // ---------- Explicit waits ----------

    public static WebElement waitForVisibility(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisibility(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String text, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForUrlContains(WebDriver driver, String fragment, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    /** FluentWait example: polls frequently and ignores transient exceptions - useful for
     *  flaky AJAX-driven rows (e.g. PIM employee list after a search). */
    public static WebElement fluentWaitForVisibility(WebDriver driver, By locator, int timeoutSeconds, int pollMillis) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until((Function<WebDriver, WebElement>) drv -> drv.findElement(locator));
    }

    // ---------- Alert handling ----------

    /** Accepts a JS alert/confirm box (e.g. the "Are you sure you want to delete" dialog). */
    public static String acceptAlert(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    public static void dismissAlertIfPresent(WebDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.dismiss();
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
            // No alert present - nothing to do.
        }
    }

    // ---------- Frame handling ----------

    public static void switchToFrame(WebDriver driver, By frameLocator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    public static void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    // ---------- Window / tab handling ----------

    public static void switchToNewWindow(WebDriver driver, String parentWindowHandle, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(parentWindowHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public static void closeCurrentAndSwitchToParent(WebDriver driver, String parentWindowHandle) {
        driver.close();
        driver.switchTo().window(parentWindowHandle);
    }
}
