package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
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
    void shouldTransferMoneyBetweenOwnCards() {
        DashboardPage.depositTo1();
        TransferOwnPage.depositTo1(transfer, account2);
        assertEquals(initialB1 + transfer, DashboardPage.getBalance(getBalance1()));
        assertEquals(initialB2 - transfer, DashboardPage.getBalance(getBalance2()));
    }
}
