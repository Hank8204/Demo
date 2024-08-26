package com.example.demo;

import com.example.demo.service.CoinDeskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CoinDeskServiceTest {

    @Autowired
    CoinDeskService coinDeskService;

    //測試呼叫 coindesk API，並顯示其內容
    @Test
    public void getCoinDesk() {
        String result = coinDeskService.getCoinDesk();
        assertNotNull(result);
        log.info(result);
    }
}
