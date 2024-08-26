package com.example.demo;

import com.example.demo.entity.Currency;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private Long currencyId;

    @BeforeEach
    public void setup(TestInfo testInfo) {
        String methodName = testInfo.getTestMethod().get().getName();
        if (methodName.equals("updateCurrency") || methodName.equals("deleteCurrency")) {
            Currency currency = new Currency();
            currency.setCode("USD");
            currency.setName("美金");
            currency.setRate(33.0);
            currency.setUpdateTime(LocalDateTime.now());
            ResponseEntity<Currency> response = testRestTemplate.postForEntity("/api/currency/add", currency, Currency.class);
            currencyId = response.getBody().getId();
            System.out.println("Currency ID: " + currencyId);
        }
    }

    //測試呼叫查詢幣別對應表資料 API，並顯示其內容。
    @Test
    public void getCurrencyList() {
        ResponseEntity<List> response = testRestTemplate.getForEntity("/api/currency/findAll", List.class);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Expected 2xx status code, but got: " + response.getStatusCode());
    }

    //測試呼叫新增幣別對應表資料 API。
    @Test
    public void createCurrency() {
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/api/currency/create", null, Void.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    //測試呼叫更新幣別對應表資料 API，並顯示其內容。
    @Test
    public void updateCurrency() {
        ResponseEntity<List<Currency>> findAllResp = testRestTemplate.exchange("/api/currency/findAll", HttpMethod.GET, null, new ParameterizedTypeReference<List<Currency>>() {});

        Currency currency = findAllResp.getBody().stream()
                .findAny()
                .orElseThrow(() -> new AssertionError("No currency found to update"));

        currency.setName("美金");

        ResponseEntity<String> response = testRestTemplate.exchange("/api/currency/update/" + currency.getId(), HttpMethod.PUT, new HttpEntity<>(currency), String.class);

        log.info("Status Code: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Expected 2xx status code, but got: " + response.getStatusCode());
    }

    //測試呼叫刪除幣別對應表資料 API。
    @Test
    public void deleteCurrency() {
        ResponseEntity<String> response = testRestTemplate.exchange("/api/currency/delete/" + currencyId, HttpMethod.DELETE, null, String.class);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Expected 2xx status code, but got: " + response.getStatusCode());
    }
}
