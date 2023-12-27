package ua.Hillel.Homework17;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.concurrent.TimeUnit;

public class WebDriverHomework {
    private WebDriver driver;
    private LoginPage loginPage;
    private MenuPage menuPage;

    @BeforeClass
    static void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void initialize() {
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        loginPage = new LoginPage(driver);
        menuPage = new MenuPage(driver);
    }

    @AfterMethod
    void tearDown() {
        driver.quit();
    }

    @Test
    public void loginAndLogout() {
        loginPage.login("standard_user", "secret_sauce");

        WebElement linkedinElement = driver.findElement(By.className("social_linkedin"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", linkedinElement);

        linkedinElement.click();

        // Отримання ідентифікаторів відкритих вкладок
        String currentWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentWindowHandle)) {
                // Переключення на нову вкладку
                driver.switchTo().window(windowHandle);

                // Отримання заголовка нової вкладки
                String linkedInTitle = driver.getTitle();
                Assert.assertEquals("Sauce Labs | LinkedIn", linkedInTitle);
                // Закриття нової вкладки
                driver.close();
            }
        }


        menuPage.openMenu();
        menuPage.logout();

        //Перевірки, щоб впевнитися що ми на сторінці Логіну
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Assert.assertNotNull("Поле вводу для логіну не знайдено", String.valueOf(usernameField));

        WebElement inputPass = driver.findElement(By.name("password"));

        WebElement loginButton2 = driver.findElement(By.id("login-button"));
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
    }
}
