package com.example.demo;

import com.example.demo.entity.Currency;
import com.example.demo.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Resource
    CurrencyService currencyService;

    @PostMapping("/create")
    public void createCurrency() {
        currencyService.createCurrency();
    }

    @PostMapping("/add")
    public Currency addCurrency(@RequestBody Currency currency) {
        return currencyService.addCurrency(currency);
    }

    @GetMapping("/findAll")
    public List<Currency> findAll() {
        return currencyService.findAll();
    }

    @GetMapping("/getById/{id}")
    public Currency getById(@PathVariable Long id) {
        return currencyService.getById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody Currency currency) {
        //檢查參數是否正確
        if (currency.getId() == null || !Objects.equals(currency.getId(), id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(currencyService.updateCurrency(currency));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        //檢查是否有此筆資料
        if (currencyService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        currencyService.deleteCurrency(id);
       //刪除成功
        return ResponseEntity.ok().build();
    }
}
