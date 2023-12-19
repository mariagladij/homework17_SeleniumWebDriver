package ua.Hillel.Homework17;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class WebDriverHomework {
    private static WebDriver driver;

    @BeforeClass
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterClass
    static void tearDown() {
        driver.quit();
    }

    @Test
    public void loginToSystem() throws InterruptedException {

        WebElement inputLoginField = driver.findElement(By.id("user-name"));
        inputLoginField.sendKeys("standard_user");

        WebElement inputPassField = driver.findElement(By.name("password"));
        inputPassField.sendKeys("secret_sauce");

        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        WebElement linkedinElement = driver.findElement(By.className("social_linkedin"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", linkedinElement);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.sleep(1000);

        linkedinElement.click();

        Thread.sleep(1000);

        // Отримання ідентифікаторів відкритих вкладок
        String currentWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentWindowHandle)) {
                // Переключення на нову вкладку
                driver.switchTo().window(windowHandle);

                // Отримання заголовка нової вкладки
                String linkedInTitle = driver.getTitle();

                System.out.println("LinkedIn Title: " + linkedInTitle);

                 Assert.assertEquals("Sauce Labs | LinkedIn", linkedInTitle);
                // Закриття нової вкладки
                driver.close();
            }
        }

        // Переключення назад на вихідну вкладку
        driver.switchTo().window(currentWindowHandle);

        WebElement menuButton = driver.findElement(By.id("react-burger-menu-btn"));
        menuButton.click();

        Thread.sleep(1000);

        WebElement logout = driver.findElement(By.id("logout_sidebar_link"));
        logout.click();

        Thread.sleep(1000);

        //Перевірки, щоб впевнитися що ми на сторінці Логіну
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Assert.assertNotNull("Поле вводу для логіну не знайдено", String.valueOf(usernameField));

        WebElement inputPass = driver.findElement(By.name("password"));
        Assert.assertNotNull("Поле вводу для пароля не знайдено", String.valueOf(inputPassField));

        WebElement loginButton2 = driver.findElement(By.id("login-button"));
        Assert.assertNotNull("Кнопка логіну не знайдена", String.valueOf(loginButton));

    }
}
