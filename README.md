# OrangeHRM Selenium + TestNG Automation Framework

Automates a slice of the OrangeHRM demo (https://opensource-demo.orangehrmlive.com/)
covering Login, PIM (employee), Leave, and Admin (user management) modules, mapped to
the manual Test Cases in `Test_Cases_OrangeHRM.xlsx`.

## Stack
- Java 11, Maven
- Selenium WebDriver 4.21
- TestNG 7.10
- WebDriverManager (auto-downloads the matching ChromeDriver binary — nothing to
  install manually)
- Page Object Model (POM)

## Project structure
```
src/test/java/com/orangehrm/
  base/    BaseTest.java       -> WebDriver setup/teardown (per-test browser)
  pages/   LoginPage, DashboardPage, PimPage, LeavePage, AdminPage
  tests/   LoginTest, AddEmployeeTest, ApplyLeaveTest, AddUserTest
  utils/   ConfigReader.java   -> reads config.properties
           WaitUtils.java      -> ALL explicit waits, alert/frame/window handling
src/test/resources/config.properties -> URL, credentials, timeout values
testng.xml     -> suite definition (run this file)
pom.xml        -> Maven dependencies
```

## Waits & handling strategy (WaitUtils.java)
- **Explicit waits** (`WebDriverWait` + `ExpectedConditions`) for visibility, clickability,
  invisibility, text presence, and URL changes — used everywhere instead of `Thread.sleep()`.
- **FluentWait** example (`fluentWaitForVisibility`) — polls every N ms and ignores
  `NoSuchElementException`/`StaleElementReferenceException`, useful after AJAX-heavy
  actions like PIM search.
- **Alert handling** — `acceptAlert` / `dismissAlertIfPresent` for native JS
  alert/confirm popups.
- **Frame handling** — `switchToFrame` / `switchToDefaultContent` for any embedded
  iframe content.
- **Window/tab handling** — `switchToNewWindow` / `closeCurrentAndSwitchToParent` for
  flows that open a new tab.
- Implicit wait is kept short (5s, see `config.properties`) as a safety net only;
  synchronization is driven by the explicit waits above, not implicit wait.

## How to run
1. Install Java 11+ and Maven.
2. Chrome must be installed; WebDriverManager handles the driver binary automatically.
3. From the project root:
   ```
   mvn clean test
   ```
   This runs `testng.xml`, which executes all four test classes in sequence.
4. To run a single module, right-click the corresponding `<test>` block in
   `testng.xml` in your IDE, or run:
   ```
   mvn clean test -Dtest=LoginTest
   ```

## Notes
- Credentials and the target URL live in `config.properties` — nothing is hardcoded
  in test or page classes.
- `AddEmployeeTest` and `AddUserTest` create real records on the public demo
  instance (it resets periodically) — re-running them will create additional
  rows rather than fail, by design of the shared demo environment.
- Add a headless flag by uncommenting the `--headless=new` line in `BaseTest.java`
  for CI pipelines.
