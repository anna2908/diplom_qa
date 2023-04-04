package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper.CardInfo;
import ru.netology.data.DBUtils;
import ru.netology.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.data.DataHelper.*;

import static com.codeborne.selenide.Selenide.*;

public class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080/");
        DBUtils.clearTables();
    }


    @Test
    @DisplayName("3.1. Успешная оплата по активной карте")
    void shouldBuy() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkApprovedNotification();
    }

    @Test
    @DisplayName("3.2. Отклонение оплаты по заблокированной карте")
    void shouldNotBuy() {
        CardInfo card = new CardInfo(getValidBlockedCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkDeclinedNotification();
    }

    @Test
    @DisplayName("3.3. Пропадание сообщений с ошибками после ввода данных")
    void shouldNotErrorVisible() {
        CardInfo card1 = new CardInfo(getEmptyNumberCard(), getEmptyMonth(), getEmptyYear(), getEmptyHolder(), getEmptyCVC());
        CardInfo card2 = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card1);
        creditPage.formInput(card2);
        creditPage.checkErrorNotVisible();
    }

    @Test
    @DisplayName("3.4. Успешная оплата по активной карте (проверка БД)")
    void shouldBuySQL() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        sleep(15000);
        assertEquals("APPROVED", DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("3.5. Отклонение оплаты по заблокированной карте (проверка БД)")
    void shouldNotBuySQL() {
        CardInfo card = new CardInfo(getValidBlockedCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        sleep(15000);
        assertEquals("DECLINED", DBUtils.getCreditStatus());
    }


    @Test
    @DisplayName("3.6. Успешная оплата по активной карте, соответствие id")
    void shouldBuySQLId() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        sleep(15000);
        assertEquals(DBUtils.getBankId(), DBUtils.getCreditId());
    }

    @Test
    @DisplayName("4.01. Ввод некорректного по формату номера карты")
    void shouldNotBuyInvalidPatternNumberCard() {
        CardInfo card = new CardInfo(getInvalidPatternNumberCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkNumberCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.02. Ввод номера карты из нулей")
    void shouldNotBuyZeroNumberCard() {
        CardInfo card = new CardInfo(getZeroNumberCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkZeroNumberCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.03. Пустое поле \"Номер карты\"")
    void shouldNotBuyEmptyNumberCard() {
        CardInfo card = new CardInfo(getEmptyNumberCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkNumberCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.04. Ввод латинских букв в номер карты")
    void shouldNotBuyLetterNumberCard() {
        CardInfo card = new CardInfo(getLetterNumberCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkNumberCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.05. Ввод специальных символов в номер карты")
    void shouldNotBuySpecialLetterNumberCard() {
        CardInfo card = new CardInfo(getSpecialLetterNumberCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkNumberCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.06. Ввод некорректного по формату значения месяца (один символ)")
    void shouldNotBuyInvalidPatternMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getInvalidPatternMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.07. Ввод некорректного значения месяца (1 проверка)")
    void shouldNotBuyZeroMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getZeroMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthTimeError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.08. Ввод некорректного значения месяца (2 проверка)")
    void shouldNotBuyInvalidMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getInvalidMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthTimeError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.09. Ввод латинских букв в значение месяца")
    void shouldNotBuyLetterMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getLetterMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.10. Ввод специальных символов в значение месяца")
    void shouldNotBuySpecialLetterMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getSpecialLetterMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.11. Пустое поле \"Месяц\"")
    void shouldNotBuyEmptyMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getEmptyMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.12. Ввод некорректного по формату значения года (один символ)")
    void shouldNotBuyInvalidPatternYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getInvalidPatternYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }


    @Test
    @DisplayName("4.13. Ввод латинских букв в значение года")
    void shouldNotBuyLetterYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getLetterYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.14. Ввод специальных символов в значение года")
    void shouldNotBuySpecialLetterYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getSpecialLetterYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.15. Пустое поле \"Год\"")
    void shouldNotBuyEmptyYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getEmptyYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.16. Ввод данных с истекшим сроком карты (проверка 1)")
    void shouldNotBuyTimeCard1() {
        CardInfo card = new CardInfo(getValidActiveCard(), getPreviousMonth(), getCurrentYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkExpiredCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.17. Ввод данных с истекшим сроком карты (проверка 2)")
    void shouldNotBuyTimeCard2() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getPreviousYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkExpiredCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.18. Ввод русских символов в поле \"Владелец\"")
    void shouldNotBuyInvalidLocaleHolder() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getInvalidLocaleHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkHolderErrorPattern();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.19. Ввод цифр в поле \"Владелец\"")
    void shouldNotBuyNumberHolder() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getInvalidNumberHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkHolderErrorPattern();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.20. Ввод специальных символов в поле \"Владелец\"")
    void shouldNotBuySpecialLetterHolder() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getSpecialLetterHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkHolderErrorPattern();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.21. Пустое поле \"Владелец\"")
    void shouldNotBuyEmptyHolder() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getEmptyHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkHolderError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.22. Ввод некорректного по формату значения CVC/CVV")
    void shouldNotBuyInvalidPatternCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getInvalidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.23. Ввод некорректного значения CVC/CVV")
    void shouldNotBuyZeroCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getZeroCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.24. Ввод латинских букв в значение CVC/CVV")
    void shouldNotBuyLetterCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getLetterCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.25. Ввод специальных символов в значение CVC/CVV")
    void shouldNotBuySpecialLetterCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getSpecialLetterCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.26. Пустое поле CVC/CVV")
    void shouldNotBuyEmptyCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getCurrentYear(), getValidHolder(), getEmptyCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("4.27. Ввод некорректного значения месяца (3 проверка)")
    void shouldNotBuyTimeCard3() {
        CardInfo card = new CardInfo(getValidActiveCard(), getZeroMonth(), getNextYear(), getValidHolder(), getValidCVC());
        val mainPage = new MainPage();
        val creditPage = mainPage.checkCredit();
        creditPage.formInput(card);
        creditPage.checkMonthTimeError();
        assertNull(DBUtils.getCreditStatus());
    }
}