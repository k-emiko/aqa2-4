package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferOwnPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    String account1 = DataHelper.getValidAccount1();
    String account2 = DataHelper.getValidAccount2();
    int initialB1;
    int initialB2;
    int transfer;
    DashboardPage dashboardPage;
    TransferOwnPage transferOwnPage;

    @BeforeAll
    static void headless(){
        Configuration.headless = true;
    }

    @BeforeEach
    void setUp(){
        open("http://localhost:9999");
        val loginPage = new LoginPage();
//    val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        initialB1 = dashboardPage.getBalance(dashboardPage.getBalance1());
        initialB2 = dashboardPage.getBalance(dashboardPage.getBalance2());
        transfer = 500;
    }

    @Test
    void transferFromCard2ToCard1() {
        transferOwnPage = dashboardPage.depositTo1();
        transferOwnPage.depositToCard(transfer, account2);
        assertEquals(initialB1 + transfer, dashboardPage.getBalance(dashboardPage.getBalance1()));
        assertEquals(initialB2 - transfer, dashboardPage.getBalance(dashboardPage.getBalance2()));
    }

    @Test
    void transferFromCard1ToCard2() {
        transferOwnPage = dashboardPage.depositTo2();
        transferOwnPage.depositToCard(transfer, account1);
        assertEquals(initialB2 + transfer, dashboardPage.getBalance(dashboardPage.getBalance2()));
        assertEquals(initialB1 - transfer, dashboardPage.getBalance(dashboardPage.getBalance1()));
    }

    @Test
    void transferFromInvalidCard() {
        transferOwnPage = dashboardPage.depositTo1();
        transferOwnPage.depositFromInvalid(transfer);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferOverdraft() {
        transfer = 10000000;
        transferOwnPage = dashboardPage.depositTo1();
        transferOwnPage.depositToCard(transfer, account2);
        transferOwnPage.assertNotificationVisibility();
    }
    @Test
    void transferToSelf() {
        transfer = 10000000;
        transferOwnPage = dashboardPage.depositTo1();
        transferOwnPage.depositToCard(transfer, account1);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferNegativeAmount() {
        transfer = -500;
        transferOwnPage = dashboardPage.depositTo2();
        transferOwnPage.depositToCard(transfer, account1);
        assertEquals(initialB2 - transfer, dashboardPage.getBalance(dashboardPage.getBalance2()));
        assertEquals(initialB1 + transfer, dashboardPage.getBalance(dashboardPage.getBalance1()));
    }

    @Test
    void transferZero() {
        transfer = 0;
        transferOwnPage = dashboardPage.depositTo2();
        transferOwnPage.depositToCard(transfer, account1);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferFromEmptyCard(){
        transfer = 1000;
        transferOwnPage = dashboardPage.depositTo2();
        transferOwnPage.depositFromEmpty(transfer);
        transferOwnPage.assertNotificationVisibility();
    }
}
