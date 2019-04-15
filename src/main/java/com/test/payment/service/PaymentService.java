package com.test.payment.service;

import com.test.payment.model.Amount;
import com.test.payment.model.Payment;
import com.test.payment.model.SubscriptionType;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.test.payment.model.SubscriptionType.*;
import static java.sql.Date.valueOf;

@Component
public class PaymentService {


    public static final String AUD = "AUD";

    public Payment retrievePayments(String amount, String type, String startDate, String endDate){
        Amount amountWithCurrency = new Amount(Integer.parseInt(amount), AUD);
        Payment payment = null;
        LocalDate sDate = null;
        LocalDate eDate = null;
        DateTimeFormatter formatter = null;
        List<Date> invoiceDates = new ArrayList<>();

        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        sDate = LocalDate.parse(startDate, formatter);
        eDate = least(LocalDate.parse(endDate, formatter), sDate.plusMonths(3));

        if(type.equalsIgnoreCase(DAILY.getText())) {
            for (LocalDate date = sDate; date.isBefore(eDate); date = date.plusDays(1)) {
                invoiceDates.add(valueOf(date));
            }
            payment = new Payment(UUID.randomUUID().toString(), amountWithCurrency, DAILY, invoiceDates);

        }
        else if(type.equalsIgnoreCase(WEEKLY.getText())){

            LocalDate skipdate = sDate;
            for (; skipdate.isBefore(eDate) && !(skipdate.getDayOfWeek() == DayOfWeek.TUESDAY); skipdate = skipdate.plusDays(1)) {

            }
            for (LocalDate date = skipdate; date.isBefore(eDate); date = date.plusDays(7)) {
                invoiceDates.add(valueOf(date));
            }
            payment = new Payment(UUID.randomUUID().toString(), amountWithCurrency, WEEKLY, invoiceDates);
        }
        else if(type.equalsIgnoreCase(SubscriptionType.MONTHLY.getText())){
            for (LocalDate date = sDate; date.isBefore(eDate); date = date.plusDays(28)) {
                for (LocalDate skipdate = date; skipdate.isBefore(eDate) && !(skipdate.getDayOfMonth() == 21); skipdate = skipdate.plusDays(1)) {
                    date = skipdate;
                }
                if(date.getDayOfMonth() == 20){
                    invoiceDates.add(valueOf(date));
                }
            }
            payment = new Payment(UUID.randomUUID().toString(), amountWithCurrency, MONTHLY, invoiceDates);
        }
        else {
            System.out.println("ERROR: INAVLID SUBSCRIPTION");

        }

        return payment;

    }

    private static LocalDate least(LocalDate a, LocalDate b) {
        return a == null ? b : (b == null ? a : (a.isBefore(b) ? a : b));
    }

}
