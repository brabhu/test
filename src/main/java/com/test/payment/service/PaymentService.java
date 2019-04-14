package com.test.payment.service;

import com.test.payment.model.Amount;
import com.test.payment.model.Payment;
import com.test.payment.model.SubscriptionType;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
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
        eDate = LocalDate.parse(endDate, formatter);

        if(type.equalsIgnoreCase(DAILY.getText())) {
            for (LocalDate date = sDate; date.isBefore(eDate); date = date.plusDays(1)) {
                invoiceDates.add(valueOf(date));
            }
            payment = new Payment(UUID.randomUUID().toString(), amountWithCurrency, DAILY, invoiceDates);

        }
        else if(type.equalsIgnoreCase(WEEKLY.getText())){

            for (LocalDate date = sDate; date.isBefore(eDate); date = date.plusDays(7)) {
                for (LocalDate skipdate = date; skipdate.isBefore(eDate) && !(skipdate.getDayOfWeek() == DayOfWeek.WEDNESDAY); skipdate = skipdate.plusDays(1)) {
                    date = skipdate;
                }
                invoiceDates.add(valueOf(date));
            }
            payment = new Payment(UUID.randomUUID().toString(), amountWithCurrency, WEEKLY, invoiceDates);
        }
        else if(type.equalsIgnoreCase(SubscriptionType.MONTHLY.getText())){
            for (LocalDate date = sDate; date.isBefore(eDate); date = date.plusDays(30)) {
                for (LocalDate skipdate = date; skipdate.isBefore(eDate) && !(skipdate.getDayOfMonth() == 21); skipdate = skipdate.plusDays(1)) {
                    date = skipdate;
                }
                invoiceDates.add(valueOf(date));
            }
            payment = new Payment(UUID.randomUUID().toString(), amountWithCurrency, MONTHLY, invoiceDates);
        }
        else {
            System.out.println("ERROR: INAVLID SUBSCRIPTION");

        }

        return payment;

    }
}
