package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collection;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.google.common.primitives.Ints.toArray;

@Value
public class DashboardPage {
    SelenideElement heading = $("[data-test-id=dashboard]");
    private Collection<SelenideElement> accounts = $$(".list__item");

    public int[] allBalances() {
        Collection<Integer> balances = new ArrayList<>();
        int accountNumber = accounts.size();
        for (int i = 0; i < accountNumber; i++) {
            balances.add(getBalance(i));
        }
        ((ArrayList<Integer>) balances).trimToSize();
        return toArray(balances);
    }

    private int getBalance(int account) {
        String tmp = String.valueOf(accounts.toArray()[account])
                .replaceAll("<.*>", "")
                .replaceAll("Пополнить", "")
                .replaceAll("р.", "")
                .replaceAll(".*:", "")
                .trim();
        return Integer.parseInt(tmp);
    }

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public TransferOwnPage depositTo(int cardNumber) {
        String tmp = "000" + cardNumber;
        if (cardNumber <= accounts.size()) {
            $(withText(tmp)).$(".button").click();
        }
        return new TransferOwnPage();
    }
}