package at.fhcampuswien.cyberpirates.WillBot.market.willhaben;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

public class WillHabenMarket implements IMarket {

    @Override
    public Result getResultForQuery(Query query) {
        return convert(getResult(convert(query)));
    }

    private WillhabenResult getResult(WillhabenQuery query) {
        return new WillhabenResult();
    }

    private WillhabenQuery convert(Query query) {
        return null;
    }

    private Result convert(WillhabenResult query) {
        return null;
    }
}
