package at.fhcampuswien.cyberpirates.WillBot.market.willhaben;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WillhabenMarket implements IMarket {

    public static void main(String[] args) {
        new WillhabenMarket();
    }

    private static final String Website = "https://www.willhaben.at";
    private Map<String, String> immo;

    public WillhabenMarket(){
        immo = new HashMap<>();

        scanImmo();
        printImmos();
    }
    private void scanImmo(){
        Document document;

        try {
            document = Jsoup.connect(Website + "/iad/immobilien/mietwohnungen/mietwohnung-angebote?" +
                    "sfId=3800c179-a691-4579-b182-97f95d41d407&isNavigation=true&page=1&rows=25").get();
        } catch (IOException ignored) {
            System.out.println("Could not scan immo.");
            return;
        }

        Elements elements = document.getElementsByClass("AnchorLink__StyledAnchor-sc-1f4x8m6-0 duRUPq " +
                "ResultListAdRowLayout___StyledClientRoutingAnchorLink-sc-1rmys2w-2 gkMyBx");

        for (Element element : elements) {
            String link = element.attributes().get("href");
            findPrice(link);
        }
    }

    private void findPrice(String link){
        Document document;

        try {
            document = Jsoup.connect(Website + link).get();
        } catch (IOException ignored) {
            System.out.println("Could not find the price for " + link);
            return;
        }
        Elements elements = document.getElementsByClass("Text-sc-10o2fdq-0 ffWioj");

        for (Element element : elements) {
            immo.put(link, element.text());
        }
    }

    private void printImmos(){
        for (Map.Entry<String, String> entry: immo.entrySet()) {
            String link = entry.getKey();
            String price = entry.getValue();

            System.out.println("Link " + Website + link);
            System.out.println("Price " + price);
        }
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
