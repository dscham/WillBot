package at.fhcampuswien.cyberpirates.WillBot.market.immowelt;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.market.URLUtil;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImmoweltMarket implements IMarket {
    private static final String RENT_URL = "https://www.immowelt.at/liste/wien/wohnungen/mieten";
    private static final String BUY_URL = "https://www.immowelt.at/liste/wien/wohnungen/kaufen";

    //functionality of methods is the same as described in the class "WillhabenMarket"
    @Override
    public List<Result> getResultsForQuery(Query query) {
        System.out.println("Immowelt: Scraping...");

        List<Integer> helper = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            helper.add(i);
        }

        return helper.parallelStream()
                .map(i -> getResultsForQueryAndPageNumber(query, i))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Result> getResultsForQueryAndPageNumber(Query query, Integer pageNumber) {
        System.out.println("Page " + pageNumber + ": Scraping...");
        ChromeOptions pageOptions = new ChromeOptions();
        pageOptions.addArguments("--headless"); // Selenium runs Chrome in background, --headless = no window
        WebDriver page = new ChromeDriver(pageOptions);

        Map<String, String> params = new HashMap<>();
        params.put("cp", pageNumber.toString());

        if (query.getPrice().getFrom() != null) {
            params.put("primi", query.getPrice().getFrom().toString());
        }
        if (query.getPrice().getTo() != null) {
            params.put("prima", query.getPrice().getTo().toString());
        }
        if (query.getLivingArea().getFrom() != null) {
            params.put("wflmi", query.getLivingArea().getFrom().toString());
        }
        if (query.getLivingArea().getTo() != null) {
            params.put("wflma", query.getLivingArea().getTo().toString());
        }
        if (query.getRoomCount().getFrom() != null) {
            params.put("roomi", query.getRoomCount().getFrom().toString());
        }
        if (query.getRoomCount().getTo() != null) {
            params.put("rooma", query.getRoomCount().getTo().toString());
        }

        // Open website
        if (query.getBuy() != null && query.getBuy()) {
            page.get(BUY_URL + URLUtil.paramMapToQueryString(params));
        } else {
            page.get(RENT_URL + URLUtil.paramMapToQueryString(params));
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scrollPage(page, pageNumber);

        System.out.println("Page " + pageNumber + ": Parsing elements...");
        List<WebElement> resultElements = page.findElements(By.cssSelector(".listitem.clear"));
        System.out.println("Page " + pageNumber + ": Parsing done.");

        if (resultElements.isEmpty()) {
            System.out.println("Page " + pageNumber + ": No elements, returning empty list.");
            page.close();
            page.quit();
            return new ArrayList<Result>();
        }

        System.out.println("Page " + pageNumber + ": Converting elements...");
        List<Result> results = resultElements.parallelStream()
                .map(element -> scrapeInfo(element, query))
                .collect(Collectors.toList());
        System.out.println("Page " + pageNumber + ": Converting done.");

        System.out.println("Page " + pageNumber + ": Scraping done. Results: " + results.size());

        page.close();
        page.quit();

        return results;
    }

    private void scrollPage(WebDriver driver, int page) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long pos = (Long) js.executeScript("return window.pageYOffset;");
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");

        System.out.println("Page " + page + ": Scrolling...");
        while (pos + 1500 < height) {
            js.executeScript("window.scrollBy(0, 300)");
            pos = (Long) js.executeScript("return window.pageYOffset;");
            height = (Long) js.executeScript("return document.body.scrollHeight;");
        }
        System.out.println("Page " + page + ": Scrolling done.");
    }

    private Result scrapeInfo(WebElement element, Query query) {
        Result result = new Result();

        result.setHref(element.findElement(By.cssSelector("a")).getAttribute("href"));

        result.setTitle(element.findElement(By.cssSelector(".listcontent.clear"))
                .findElement(By.cssSelector("h2")).getText());

        String price =
                element.findElement(By.cssSelector(".hardfacts_3.clear"))
                        .findElement(By.cssSelector(".hardfact.price_rent strong")).getText();
        result.setPrice(price);

        String livingArea =
                element.findElement(By.cssSelector(".hardfacts_3.clear"))
                        .findElement(By.cssSelector(".hardfact.square_meters")).getText();
        result.setLivingArea(livingArea);

        String roomCount = element.findElement(By.cssSelector(".hardfacts_3.clear"))
                .findElement(By.cssSelector(".hardfact.rooms")).getText();
        result.setRoomCount(roomCount);

        result.setBuy(query.getBuy() == null ? "false" : query.getBuy().toString());
        result.setPostCode("Wien");
        result.setProvider("Immowelt");

        return result;
    }
}