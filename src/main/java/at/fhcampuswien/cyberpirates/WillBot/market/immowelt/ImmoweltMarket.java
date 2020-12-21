package at.fhcampuswien.cyberpirates.WillBot.market.immowelt;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

public class ImmoweltMarket implements IMarket {

    @Override
    public Result getResultForQuery(Query query) {
        return convert(getResult(convert(query)));
    }

    private ImmoweltResult getResult(ImmoweltQuery query) {
        return new ImmoweltResult();
    }

    private ImmoweltQuery convert(Query query) {
        return null;
    }

    private Result convert(ImmoweltResult query) {
        return null;
    }
}

