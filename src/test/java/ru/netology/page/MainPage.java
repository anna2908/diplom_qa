package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private SelenideElement paymentButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));

    public PaymentPage checkPayment() {
        paymentButton.click();
        return new PaymentPage();
    }

}
