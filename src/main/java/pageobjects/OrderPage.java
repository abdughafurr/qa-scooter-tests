package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object страницы заказа самоката.
 * <p>
 * Список элементов:
 * // Шаг 1 «Для кого самокат»
 * //   Поле «Имя»
 * //   Поле «Фамилия»
 * //   Поле «Адрес»
 * //   Поле «Станция метро»
 * //   Выпадающий список со станциями
 * //   Поле «Телефон»
 * //   Кнопка «Далее»
 * // Шаг 2 «Про аренду»
 * //   Поле «Когда привезти самокат»
 * //   Поле «Срок аренды» (выпадающий список)
 * //   Чекбоксы цвета: «чёрный жемчуг», «серая безысходность»
 * //   Поле «Комментарий для курьера»
 * //   Кнопка «Заказать»
 * // Попап подтверждения заказа
 * //   Кнопка «Да»
 * // Финальный попап «Заказ оформлен»
 */
public class OrderPage {

    private final WebDriver driver;

    // ---- Шаг 1 ----
    private final By firstNameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By lastNameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroStationField = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // ---- Шаг 2 ----
    private final By deliveryDateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdown = By.xpath(".//div[text()='* Срок аренды']");
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    // Кнопка «Заказать» на втором шаге — берём последнюю на странице (она внизу формы)
    private final By orderButton = By.xpath("(.//button[text()='Заказать'])[last()]");

    // ---- Попап подтверждения ----
    private final By confirmYesButton = By.xpath(".//button[text()='Да']");

    // ---- Финальный попап «Заказ оформлен» ----
    private final By orderPlacedHeader =
            By.xpath(".//div[contains(text(),'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // ===== Шаг 1 =====

    public void fillFirstStep(String firstName, String lastName, String address,
                              String metroStation, String phone) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(firstNameField))
                .sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(addressField).sendKeys(address);
        selectMetroStation(metroStation);
        driver.findElement(phoneField).sendKeys(phone);
    }

    private void selectMetroStation(String station) {
        WebElement metroInput = driver.findElement(metroStationField);
        metroInput.click();
        // Кликаем по нужной станции в выпадающем списке по её названию
        By stationOption = By.xpath(".//div[contains(@class,'Order_Text') and text()='" + station + "']");
        WebElement option = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(stationOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(stationOption))
                .click();
    }

    public void clickNextButton() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(nextButton))
                .click();
    }

    // ===== Шаг 2 =====

    public void fillSecondStep(String date, String rentalPeriod, String color, String comment) {
        // Дата доставки
        WebElement dateInput = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(deliveryDateField));
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);

        // Срок аренды — кастомный dropdown
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(rentalPeriodDropdown))
                .click();
        By rentalOption = By.xpath(".//div[contains(@class,'Dropdown-option') and text()='" + rentalPeriod + "']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(rentalOption))
                .click();

        // Цвет — чекбокс (если задан)
        if (color != null && !color.isEmpty()) {
            By colorCheckbox = By.xpath(".//label[text()='" + color + "']");
            driver.findElement(colorCheckbox).click();
        }

        // Комментарий
        if (comment != null && !comment.isEmpty()) {
            driver.findElement(commentField).sendKeys(comment);
        }
    }

    public void clickOrderButton() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
    }

    public void confirmOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(confirmYesButton))
                .click();
    }

    // Проверка, что появился финальный попап
    public boolean isOrderPlacedPopupVisible() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderPlacedHeader));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}