package at.fhcampuswien.cyberpirates.WillBot.service;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.market.willhaben.WillhabenMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    List<IMarket> markets = new ArrayList<>();

    Service() {
        markets.add(new WillhabenMarket());
    }

    List<Result> getResult(Query query) {
        return markets.stream()
                .map(market -> market.getResultForQuery(query))
                .collect(Collectors.toList());
    }
}
