package com.example.demo.service;

import com.example.demo.Repository.CurrencyRepository;
import com.example.demo.entity.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class CurrencyService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CoinDeskService coinDeskService;
    @Autowired
    private CurrencyRepository currencyRepository;


    public void createCurrency() {
        //取得資料
        String result = coinDeskService.getCoinDesk();
        //轉換JsonNode
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //取得時間、貨幣名稱、貨幣價格
        String time = jsonNode.get("time").get("updatedISO").asText();
        String dollarRate = jsonNode.get("bpi").get("USD").get("rate").asText();
        String euroRate = jsonNode.get("bpi").get("EUR").get("rate").asText();
        String gbpRate = jsonNode.get("bpi").get("GBP").get("rate").asText();
        //放入List
        List<Currency> currencyList = new ArrayList<>();
        Currency USD = new Currency();
        USD.setCode("USD");
        USD.setName("美金");
        USD.setRate(Double.parseDouble(dollarRate.replace(",", "")));
        USD.setUpdateTime(convertToLocalDateTime(time));
        currencyList.add(USD);
        Currency EUR = new Currency();
        EUR.setCode("EUR");
        EUR.setName("歐元");
        EUR.setRate(Double.parseDouble(euroRate.replace(",", "")));
        EUR.setUpdateTime(convertToLocalDateTime(time));
        currencyList.add(EUR);
        Currency GBP = new Currency();
        GBP.setCode("GBP");
        GBP.setName("英鎊");
        GBP.setRate(Double.parseDouble(gbpRate.replace(",", "")));
        GBP.setUpdateTime(convertToLocalDateTime(time));
        currencyList.add(GBP);
        currencyRepository.saveAll(currencyList);
    }

    public static LocalDateTime convertToLocalDateTime(String isoTimeString) {
        // 使用 ISO_OFFSET_DATE_TIME 解析 ISO 8601 格式的時間字串
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // 轉換為 LocalDateTime
        return offsetDateTime.toLocalDateTime();
    }

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Currency getById(Long id) {
        return currencyRepository.findById(id).orElse(null);
    }

    public Currency updateCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public void deleteCurrency(Long id) {
        currencyRepository.deleteById(id);
    }

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }
}
