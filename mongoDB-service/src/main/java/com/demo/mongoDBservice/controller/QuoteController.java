package com.demo.mongoDBservice.controller;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.mongoDBservice.bean.Quote;
import com.demo.mongoDBservice.bean.Quotes;
import com.demo.mongoDBservice.repository.QuoteRepository;

@RestController
@RequestMapping("/db/quotes")
public class QuoteController
{

    @Autowired
    QuoteRepository quoteRepository;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public List<String> getQuotes (@PathVariable String name)
    {
        return getQuotesByName(name);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Quote> getAllQuotes ()
    {
        return quoteRepository.findAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public List<String> add (@RequestBody
    final Quotes quotes)
    {

        quotes.getQuotes()
            .stream()
            .map(quote -> new Quote(quotes.getName(), quote, new Random().nextInt(1000)))
            .forEach(quote -> quoteRepository.save(quote));
        return getQuotesByName(quotes.getName());
    }

    @RequestMapping(value = "/delete/{name}", method = RequestMethod.DELETE)
    public List<String> delete (@PathVariable("name")
    final String name)
    {

        List<Quote> quotes = quoteRepository.findByName(name);
        // quoteRepository.delete((Quote)quotes);
        quotes.stream()
            .forEach(quote -> quoteRepository.delete(quote));
        return getQuotesByName(name);
    }

    private List<String> getQuotesByName (@PathVariable("name") String name)
    {
        return quoteRepository.findByName(name)
            .stream()
            .map(Quote::getQuote)
            .collect(Collectors.toList());
    }

}
