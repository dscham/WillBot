package at.fhcampuswien.cyberpirates.WillBot.market;

import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

public interface IMarket {

    public Result getResultForQuery(Query query);
}
