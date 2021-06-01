package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferOwnPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.page.DashboardPage.getBalance1;
import static ru.netology.web.page.DashboardPage.getBalance2;

class MoneyTransferTest {
    String account1 = "5559 0000 0000 0001";
    String account2 = "5559 0000 0000 0002";
    int initialB1;
    int initialB2;
    int transfer;

    @BeforeEach
    void setUp(){
        open("http://localhost:9999");
        val loginPage = new LoginPage();
//    val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        initialB1 = DashboardPage.getBalance(getBalance1());
        initialB2 = DashboardPage.getBalance(getBalance2());
        transfer = 500;
    }

    @Test
    void transferFromCard2ToCard1() {
        DashboardPage.depositTo1();
        TransferOwnPage.depositToCard(transfer, account2);
        assertEquals(initialB1 + 100, DashboardPage.getBalance(getBalance1()));
        assertEquals(initialB2 - transfer, DashboardPage.getBalance(getBalance2()));
    }

    @Test
    void transferFromCard1ToCard2() {
        DashboardPage.depositTo2();
        TransferOwnPage.depositToCard(transfer, account1);
        assertEquals(initialB2 + 100, DashboardPage.getBalance(getBalance2()));
        assertEquals(initialB1 - transfer, DashboardPage.getBalance(getBalance1()));
    }

    @Test
    void transferFromInvalidCard() {
        DashboardPage.depositTo1();
        TransferOwnPage.depositToCard(transfer, "0000 0000 0000 0000");
        TransferOwnPage.assertNotificationVisibility();
    }

    @Test @Disabled
    void transferOverdraft() {
        transfer = 10000000;
        DashboardPage.depositTo1();
        TransferOwnPage.depositToCard(transfer, account2);
        TransferOwnPage.assertNotificationVisibility();
    }
    @Test @Disabled
    void transferToSelf() {
        transfer = 10000000;
        DashboardPage.depositTo1();
        TransferOwnPage.depositToCard(transfer, account1);
        TransferOwnPage.assertNotificationVisibility();
    }
    @Test
    void transferNegativeAmount() {
        transfer = -500;
        DashboardPage.depositTo2();
        TransferOwnPage.depositToCard(transfer, account1);
        assertEquals(initialB2 - 100, DashboardPage.getBalance(getBalance2()));
        assertEquals(initialB1 + transfer, DashboardPage.getBalance(getBalance1()));
    }

    @Test @Disabled
    void transferZero() {
        transfer = 0;
        DashboardPage.depositTo2();
        TransferOwnPage.depositToCard(transfer, account1);
        TransferOwnPage.assertNotificationVisibility();
    }
}
