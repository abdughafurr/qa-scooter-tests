package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pageobjects.MainPage;
import pageobjects.OrderPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Параметризованные тесты позитивного сценария оформления заказа.
 * <p>
 * Проверяем две точки входа (верхняя и нижняя кнопки «Заказать»)
 * с двумя разными наборами данных.
 * <p>
 * Параметризация по entryPoint покрывает требование задания:
 * «Проверить точки входа в сценарий, их две».
 */
@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {

    private final String entryPoint;     // "top" или "middle"
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final String rentalPeriod;
    private final String color;
    private final String comment;

    public OrderTest(String entryPoint,
                     String firstName, String lastName, String address,
                     String metroStation, String phone,
                     String date, String rentalPeriod, String color, String comment) {
        this.entryPoint = entryPoint;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Заказ через {0}: {1} {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Набор 1: заказ через верхнюю кнопку
                {
                        "top",
                        "Иван", "Иванов", "Москва, Тверская 1",
                        "Сокольники", "+79991234567",
                        "01.06.2026", "сутки", "чёрный жемчуг", "Позвонить за час"
                },
                // Набор 2: заказ через нижнюю кнопку
                {
                        "middle",
                        "Мария", "Петрова", "Санкт-Петербург, Невский 100",
                        "Черкизовская", "+79997654321",
                        "15.07.2026", "двое суток", "серая безысходность", "Оставить у консьержа"
                }
        });
    }

    @Test
    public void orderCanBePlacedSuccessfully() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.acceptCookiesIfPresent();

        // Выбираем точку входа
        if ("top".equals(entryPoint)) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickMiddleOrderButton();
        }

        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillFirstStep(firstName, lastName, address, metroStation, phone);
        orderPage.clickNextButton();

        orderPage.fillSecondStep(date, rentalPeriod, color, comment);
        orderPage.clickOrderButton();
        orderPage.confirmOrder();

        assertTrue("Окно с подтверждением успешного заказа не появилось",
                orderPage.isOrderPlacedPopupVisible());
    }
}
