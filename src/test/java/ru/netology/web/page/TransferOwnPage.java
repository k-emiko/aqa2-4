package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

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

    public void depositToCard(int sum, String account) {
        amount.setValue(String.valueOf(sum));
        from.setValue(account);
        transfer.click();
    }

    public void depositFromEmpty(int sum) {
        amount.setValue(String.valueOf(sum));
        transfer.click();
    }

    public void depositFromInvalid(int sum) {
        amount.setValue(String.valueOf(sum));
        from.setValue(DataHelper.getInvalidAccount());
        transfer.click();
    }

    public void assertNotificationVisibility() {
        notification.shouldBe(visible).shouldHave(text("Ошибка"));
    }

}
