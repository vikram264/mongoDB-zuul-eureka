package com.demo.stockservice.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/stock")
public class StockController
{

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{name}")
    public List<Quote> getStock (@PathVariable("name")
    final String name)
    {

        ResponseEntity<List<String>> quoteResponse =
            restTemplate.exchange("http://mongo-db-service/db/quotes/"
                + name, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});

        List<String> quotes = quoteResponse.getBody();
        return quotes
            .stream()
            .map(quote->{
                Stock stock = getStockPrice(quote);
                return new Quote(quote, stock.getQuote().getPrice());
            })
            //.map(this::getStockPrice)
            .collect(Collectors.toList());
    }

    private Stock getStockPrice (String quote)
    {
        try {
            return YahooFinance.get(quote);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Stock(quote);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private class Quote
    {
        private String quote;
        private BigDecimal price;
    }
}
