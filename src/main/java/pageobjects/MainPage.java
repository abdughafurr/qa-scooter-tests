package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object главной страницы Яндекс.Самокат.
 * <p>
 * Список элементов страницы:
 * // Логотип Самоката (слева в шапке)
 * // Логотип Яндекса (справа от логотипа Самоката)
 * // Кнопка «Заказать» в шапке (верхняя)
 * // Кнопка «Заказать» в середине страницы (нижняя)
 * // Кнопка «Статус заказа» в шапке
 * // Поле ввода номера заказа
 * // Кнопка «Go!» (поиск статуса заказа)
 * // Раздел «Вопросы о важном» — заголовки (8 штук)
 * // Раздел «Вопросы о важном» — тексты ответов (8 штук)
 */
public class MainPage {

    private final WebDriver driver;

    // Логотип Самоката
    private final By scooterLogo = By.xpath(".//img[@alt='Scooter']");

    // Логотип Яндекса
    private final By yandexLogo = By.xpath(".//img[@alt='Yandex']");

    // Верхняя кнопка «Заказать» (в шапке)
    private final By orderButtonTop = By.className("Button_Button__ra12g");

    // Нижняя кнопка «Заказать» (в середине страницы)
    private final By orderButtonMiddle =
            By.xpath(".//div[contains(@class,'Home_FinishButton')]/button[text()='Заказать']");

    // Кнопка «Принять» в куки-баннере
    private final By cookieAcceptButton = By.id("rcc-confirm-button");

    // Раздел «Вопросы о важном» — нужно проскроллить до него, чтобы видеть аккордеон
    private final By importantQuestionsSection = By.className("Home_FAQ__3uVm4");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    // Открыть главную страницу
    public void open() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    // Закрыть куки-баннер, если он есть
    public void acceptCookiesIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(cookieAcceptButton))
                    .click();
        } catch (Exception ignored) {
            // баннера может не быть — это нормально
        }
    }

    // Клик по верхней кнопке «Заказать»
    public void clickTopOrderButton() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(orderButtonTop))
                .click();
    }

    // Клик по нижней кнопке «Заказать» (с прокруткой)
    public void clickMiddleOrderButton() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(orderButtonMiddle));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", button);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(orderButtonMiddle))
                .click();
    }

    // Клик по заголовку вопроса в FAQ по индексу (0..7)
    public void clickFaqQuestion(int index) {
        By questionLocator = By.id("accordion__heading-" + index);
        WebElement question = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(questionLocator));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", question);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(questionLocator))
                .click();
    }

    // Получить текст ответа FAQ по индексу
    public String getFaqAnswerText(int index) {
        By answerLocator = By.id("accordion__panel-" + index);
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(answerLocator))
                .getText();
    }

    // Клик по логотипу Самоката
    public void clickScooterLogo() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(scooterLogo))
                .click();
    }

    // Клик по логотипу Яндекса
    public void clickYandexLogo() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(yandexLogo))
                .click();
    }
}
