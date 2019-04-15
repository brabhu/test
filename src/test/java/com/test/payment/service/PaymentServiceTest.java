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
    public void shouldMaxSubscriptionIsThreeMonthsForMonthlySubscription(){

        Payment payment = paymentService.retrievePayments("100", MONTHLY.getText(), "18-01-2020", "28-05-2020");

        Date expectedDate1 = valueOf(LocalDate.parse("20-01-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate1.equals(payment.getInvoice_dates().get(0)));
        Date expectedDate2 = valueOf(LocalDate.parse("20-02-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate2.equals(payment.getInvoice_dates().get(1)));
        Date expectedDate3 = valueOf(LocalDate.parse("20-03-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedDate3.equals(payment.getInvoice_dates().get(2)));
        assert(payment.getInvoice_dates().size() == 3);

    }

    @Test
    public void shouldMaxSubscriptionIsThreeMonthsForDailySubscriptionForLeapYear(){

        Payment payment = paymentService.retrievePayments("100", DAILY.getText(), "18-01-2020", "28-05-2020");

        Date expectedStartDate = valueOf(LocalDate.parse("18-01-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedStartDate.equals(payment.getInvoice_dates().get(0)));

        Date expectedEndDate = valueOf(LocalDate.parse("17-04-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedEndDate.equals(payment.getInvoice_dates().get(90)));
        assert(payment.getInvoice_dates().size() == 91);
    }

    @Test
    public void shouldMaxSubscriptionIsThreeMonthsForDailySubscription(){

        Payment payment = paymentService.retrievePayments("100", DAILY.getText(), "18-01-2021", "28-05-2021");

        Date expectedStartDate = valueOf(LocalDate.parse("18-01-2021", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedStartDate.equals(payment.getInvoice_dates().get(0)));

        Date expectedEndDate = valueOf(LocalDate.parse("17-04-2021", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assert(expectedEndDate.equals(payment.getInvoice_dates().get(89)));
        assert(payment.getInvoice_dates().size() == 90);
    }

    @Test
    public void shouldIncludeLeapYearSubscription(){

        Payment payment = paymentService.retrievePayments("100", DAILY.getText(), "28-02-2020", "01-03-2020");

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