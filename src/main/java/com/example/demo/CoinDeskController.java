package com.example.demo;

import com.example.demo.service.CoinDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {

    @Autowired
    CoinDeskService coinDeskService;

    @GetMapping("/getData")
    public String getCoinDesk() {
        return coinDeskService.getCoinDesk();
    }

}
