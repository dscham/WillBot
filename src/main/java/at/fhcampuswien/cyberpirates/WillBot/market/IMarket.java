package at.fhcampuswien.cyberpirates.WillBot.market;

import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

import java.util.List;

public interface IMarket {

    List<Result> getResultsForQuery(Query query);
}
