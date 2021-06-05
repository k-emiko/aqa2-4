package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Value;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
@Value
public class DashboardPage {
    SelenideElement heading = $("[data-test-id=dashboard]");
    private static SelenideElement transferButton1 = $$(".button[data-test-id='action-deposit']").first();
    private static SelenideElement transferButton2 = $$(".button[data-test-id='action-deposit']").last();
    private static SelenideElement balance1 = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private static SelenideElement balance2 = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");

    public SelenideElement getBalance1() {
        return balance1;
    }

    public SelenideElement getBalance2() {
        return balance2;
    }

    public int getBalance(SelenideElement account) {
        String tmp = account.getOwnText();
        int index;
        for (int i = 0; i < 2; i++) {
            index = tmp.indexOf('\n');
            tmp = tmp.substring(index + 1);
        }
        index = tmp.indexOf('\n');
        tmp = tmp.substring(0, index);
        return Integer.parseInt(tmp);
    }

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public TransferOwnPage depositTo(String cardNumber){
        if (cardNumber == "1"){
            transferButton1.click();
        }
        if (cardNumber == "2"){
            transferButton2.click();
        }
        return new TransferOwnPage();
    }
}