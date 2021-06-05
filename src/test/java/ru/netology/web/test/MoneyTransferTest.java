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
    String[] accounts = DataHelper.getValidAccounts();
    int transfer;
    DashboardPage dashboardPage;
    TransferOwnPage transferOwnPage;
    int[] initialBalances;

    @BeforeAll
    static void headless() {
        Configuration.headless = true;
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        initialBalances = dashboardPage.allBalances();
        transfer = 500;
    }

    @Test
    void transferFromCard2ToCard1() {
        transferOwnPage = dashboardPage.depositTo(1);
        transferOwnPage.depositToCard(transfer, accounts[1]);
        assertEquals(initialBalances[0] + transfer, dashboardPage.allBalances()[0]);
        assertEquals(initialBalances[1] - transfer, dashboardPage.allBalances()[1]);
    }

    @Test
    void transferFromCard1ToCard2() {
        transferOwnPage = dashboardPage.depositTo(2);
        transferOwnPage.depositToCard(transfer, accounts[0]);
        assertEquals(initialBalances[1] + transfer, dashboardPage.allBalances()[1]);
        assertEquals(initialBalances[0] - transfer, dashboardPage.allBalances()[0]);
    }

    @Test
    void transferFromInvalidCard() {
        transferOwnPage = dashboardPage.depositTo(1);
        transferOwnPage.depositFromInvalid(transfer);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferOverdraft() {
        transfer = 10000000;
        transferOwnPage = dashboardPage.depositTo(1);
        transferOwnPage.depositToCard(transfer, accounts[1]);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferToSelf() {
        transfer = 10000000;
        transferOwnPage = dashboardPage.depositTo(1);
        transferOwnPage.depositToCard(transfer, accounts[0]);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferNegativeAmount() {
        transfer = -500;
        transferOwnPage = dashboardPage.depositTo(2);
        transferOwnPage.depositToCard(transfer, accounts[0]);
        assertEquals(initialBalances[1] - transfer, dashboardPage.allBalances()[1]);
        assertEquals(initialBalances[0] + transfer, dashboardPage.allBalances()[0]);
    }

    @Test
    void transferZero() {
        transfer = 0;
        transferOwnPage = dashboardPage.depositTo(2);
        transferOwnPage.depositToCard(transfer, accounts[0]);
        transferOwnPage.assertNotificationVisibility();
    }

    @Test
    void transferFromEmptyCard() {
        transfer = 1000;
        transferOwnPage = dashboardPage.depositTo(2);
        transferOwnPage.depositFromEmpty(transfer);
        transferOwnPage.assertNotificationVisibility();
    }
}
