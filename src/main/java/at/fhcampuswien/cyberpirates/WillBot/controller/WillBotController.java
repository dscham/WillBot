package at.fhcampuswien.cyberpirates.WillBot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;
import at.fhcampuswien.cyberpirates.WillBot.service.WillBotService;

@RestController
@RequestMapping("/api")
public class WillBotController {

    @Autowired
    WillBotService service;

    @GetMapping("request")
    public ResponseEntity<List<Result>> request(
            @RequestParam Integer postCode,
            @RequestParam Double priceTo,
            @RequestParam(required = false) Double priceFrom,
            @RequestParam(required = false) Double livingAreaFrom,
            @RequestParam(required = false) Double livingAreaTo,
            @RequestParam(required = false) Integer roomCountFrom,
            @RequestParam(required = false) Integer roomCountTo,
            @RequestParam(required = false) Boolean buy) {
        Query query = new Query();

        query.setBuy(buy);
        query.setPostCode(postCode);
        query.getPrice().setFrom(priceFrom);
        query.getPrice().setTo(priceTo);
        query.getLivingArea().setFrom(livingAreaFrom);
        query.getLivingArea().setTo(livingAreaTo);
        query.getRoomCount().setFrom(roomCountFrom);
        query.getRoomCount().setTo(roomCountTo);

        return ResponseEntity.ok(service.runRequest(query));
    }

    @PutMapping("json/request")
    public ResponseEntity<List<Result>> jsonRequest(@RequestBody Query query) {
        return ResponseEntity.ok(service.runRequest(query));
    }
}
