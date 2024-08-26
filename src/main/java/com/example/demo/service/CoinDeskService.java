package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoinDeskService {
    public String getCoinDesk() {
        //打API 取得資料
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        //檢查是否有資料 否則拋出錯誤
        String result = restTemplate.getForObject(url, String.class);
        if (result == null) {
            throw new RuntimeException("取得資料失敗");
        }
        return result;
    }
}
