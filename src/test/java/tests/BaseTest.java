package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Базовый класс для всех тестов.
 * Инициализирует Chrome перед тестом и закрывает браузер после.
 * Если хочешь запускать в Firefox — раскомментируй блок ниже.
 */
public class BaseTest {

    protected WebDriver driver;

    @Before
    public void setUp() {
        // ===== Chrome (по умолчанию) =====
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);

        // ===== Firefox (закомментировано) =====
        // WebDriverManager.firefoxdriver().setup();
        // driver = new FirefoxDriver();
        // driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
