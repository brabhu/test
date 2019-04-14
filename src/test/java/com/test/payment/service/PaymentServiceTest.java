package com.test.payment.service;

import com.test.payment.model.Payment;
import com.test.payment.model.SubscriptionType;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.test.payment.model.SubscriptionType.*;
import static java.sql.Date.valueOf;
import static org.junit.Assert.*;



public class PaymentServiceTest {

    PaymentService paymentService;

    @Before
    public void setUp() throws Exception {
        paymentService = new PaymentService();
    }

    @Test
    public void shouldStartOnTuesdayForWeeklySubscription(){
        Payment payment = paymentService.retrievePayments("100", WEEKLY.getText(), "23-11-2019", "28-11-2019");

        Date expectedDate = valueOf(LocalDate.parse("26-11-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate.equals(payment.getInvoice_dates().get(0)));
        assert(payment.getInvoice_dates().size() == 1);
    }

    @Test
    public void shouldStartOnTwentyForMonthlySubscription(){
        Payment payment = paymentService.retrievePayments("100", MONTHLY.getText(), "18-11-2019", "28-11-2019");

        Date expectedDate = valueOf(LocalDate.parse("20-11-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate.equals(payment.getInvoice_dates().get(0)));
        assert(payment.getInvoice_dates().size() == 1);
    }

    @Test
    public void shouldBeWithinDatesForSubscription(){

        Payment payment = paymentService.retrievePayments("100", DAILY.getText(), "18-11-2019", "28-11-2019");

        Date expectedDate = valueOf(LocalDate.parse("18-11-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate.equals(payment.getInvoice_dates().get(0)));
        Date lastDate = valueOf(LocalDate.parse("27-11-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(lastDate.equals(payment.getInvoice_dates().get(9)));
        assert(payment.getInvoice_dates().size() == 10);

    }

    @Test
    public void shouldIncludeLeapYearSubscription(){

        Payment payment = paymentService.retrievePayments("100", DAILY.getText(), "28-02-2020", "1-03-2020");

        Date expectedDate = valueOf(LocalDate.parse("28-02-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate.equals(payment.getInvoice_dates().get(0)));
        Date lastDate = valueOf(LocalDate.parse("29-02-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(lastDate.equals(payment.getInvoice_dates().get(1)));
        assert(payment.getInvoice_dates().size() == 2);

    }

//    @Test(expected=IllegalArgumentException.class)
//    public void shouldResultInException() throws Exception{
//
//        paymentService.retrievePayments("100", DAILY.getText(), "28-02-2020", "01-03-2020");
//    }

}