package at.fhcampuswien.cyberpirates.WillBot.service;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.market.immowelt.ImmoweltMarket;
import at.fhcampuswien.cyberpirates.WillBot.market.willhaben.WillhabenMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WillBotService {
    List<IMarket> markets = new ArrayList<>();

    WillBotService() {
        markets.add(new WillhabenMarket());
        markets.add(new ImmoweltMarket());
    }

    public List<Result> runRequest(Query query) {
        return markets.stream()
                .flatMap(market -> market.getResultsForQuery(query).stream())
                .collect(Collectors.toList());
    }
}
