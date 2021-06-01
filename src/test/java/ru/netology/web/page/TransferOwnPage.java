package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferOwnPage {
    private static SelenideElement amount = $("[data-test-id='amount'] .input__control");
    private static SelenideElement from = $("[data-test-id='from'] .input__control");
    private static SelenideElement transfer = $("[data-test-id='action-transfer'].button");
    private static SelenideElement notification = $(".notification");

    public TransferOwnPage() {
        $("[data-test-id='amount'").shouldBe(visible);
    }

    public static void depositToCard(int sum, String account){
        amount.setValue(String.valueOf(sum));
        from.setValue(account);
        transfer.click();
    }
    public static void assertNotificationVisibility(){
        notification.shouldBe(visible).shouldHave(text("Ошибкt"));
    }

}
