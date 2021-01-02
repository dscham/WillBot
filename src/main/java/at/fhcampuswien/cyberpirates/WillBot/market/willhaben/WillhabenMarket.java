package at.fhcampuswien.cyberpirates.WillBot.market.willhaben;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;


public class WillhabenMarket implements IMarket {

    public static void main(String[] args) throws Exception {
        WillhabenMarket market = new WillhabenMarket();
        market.getResult(null);
    }

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
