package com.test.payment.controller;

import com.test.payment.model.Amount;
import com.test.payment.model.Payment;
import com.test.payment.model.SubscriptionType;
import com.test.payment.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import static com.test.payment.service.PaymentService.AUD;
import static java.sql.Date.valueOf;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PaymentController.class, secure = false)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    Payment mockPayment = new Payment("id", new Amount(Integer.parseInt("100"), AUD), SubscriptionType.MONTHLY,
            Arrays.asList(valueOf(LocalDate.parse("20-11-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")))));


    @Test
    public void retrieveInvoiceForMonth() throws Exception {

        Mockito.when(paymentService.retrievePayments(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString())).thenReturn(mockPayment);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/payments/100/types/MONTHLY/start/18-11-2019/end/27-11-2019").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"id\":\"id\",\"amount\":{\"value\":100,\"currency\":\"AUD\"},\"subscription_type\":\"MONTHLY\",\"invoice_dates\":[\"2019-11-20\"]}";


        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }
}
