package at.fhcampuswien.cyberpirates.WillBot.market.willhaben;

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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WillhabenMarket implements IMarket {
    private static final String RENT_URL = "https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote";
    private static final String BUY_URL = "https://www.willhaben.at/iad/immobilien/eigentumswohnung/eigentumswohnung-angebote";

    @Override
    public List<Result> getResultsForQuery(Query query) {
        System.out.println("Willhaben: Scraping...");

        //loop used to go through the pages on the specific website
        List<Integer> helper = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            helper.add(i);
        }

        return helper.parallelStream()
                .map(i -> getResultsForQueryAndPage(query, i))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Result> getResultsForQueryAndPage(Query query, Integer pageNumber) {
        System.out.println("Page " + pageNumber + ": Scraping...");
        ChromeOptions pageOptions = new ChromeOptions();
        pageOptions.addArguments("--headless"); // Selenium runs Chrome in background, --headless = no window
        WebDriver page = new ChromeDriver(pageOptions);

        //to get the specific page, parameters need to be added to the original website
        Map<String, String> params = new HashMap<>();
        params.put("sfId", "3800c179-a691-4579-b182-97f95d41d407");
        params.put("isNavigation", "true");
        params.put("page", pageNumber.toString());
        params.put("rows", "200");

        if (query.getPostCode() != null) {
            params.put("areaId", query.getPostCode().toString());
        }
        if (query.getPrice().getFrom() != null) {
            params.put("PRICE_FROM", query.getPrice().getFrom().toString());
        }
        if (query.getPrice().getTo() != null) {
            params.put("PRICE_TO", query.getPrice().getTo().toString());
        }
        if (query.getLivingArea().getFrom() != null) {
            params.put("ESTATE_SIZE/LIVING_AREA_FROM", query.getLivingArea().getFrom().toString());
        }
        if (query.getLivingArea().getTo() != null) {
            params.put("ESTATE_SIZE/LIVING_AREA_TO", query.getLivingArea().getTo().toString());
        }
        if (query.getRoomCount().getTo() != null) {
            params.put("NO_OF_ROOMS_BUCKET", query.getRoomCount().getTo() + "X" + query.getRoomCount().getTo());
        }

        // if statement to either get data from buy or rent website
        if (query.getBuy() != null && query.getBuy()) {
            page.get(BUY_URL + URLUtil.paramMapToQueryString(params));
        } else {
            page.get(RENT_URL + URLUtil.paramMapToQueryString(params));
        }

        //once a website is opened, this thread statement allows the code to wait 0,5 seconds before it does anything else
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Page " + pageNumber + ": Consent waiting...");
        /* since Willhaben has a Pop-up window where cookies need to be accepted, with this function once the website is
           opened, the button will be automatically pressed so the code can further do the work we want */
        List<WebElement> cookieConsent =
                page.findElements(By.xpath("//*[@class='didomi-components-button didomi-button didomi-dismiss-button didomi-" +
                        "components-button--color didomi-button-highlight highlight-button']")); //close the cookies pop-up
        cookieConsent.get(0).click();
        System.out.println("Page " + pageNumber + ": Consent given.");

        scrollPage(page, pageNumber);

        System.out.println("Page " + pageNumber + ": Parsing elements...");
        List<WebElement> resultElements = page.findElements(By.cssSelector(".Box-wfmb7k-0.iRWwJP.gnPcbh"));
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

    /* since Willhaben is a dynamic website written in a javascipt we have to scroll through it to get all elements,
       otherwise the output would be only 5 results per page. This way we can enable the programme to jump every 300
       pixel until the end to grab every real estate that can be found on the website   */
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

    /* this method allows us to scrape the information from the website which is being scrolled through like title of the
       the post, size, room count, price and a link so the user is not required to search it but can simply press the link
       and the website of the specific real estate will be automatically opened */
    private Result scrapeInfo(WebElement element, Query query) {
        Result result = new Result();

        result.setTitle(element.findElement(By.cssSelector(".Box-wfmb7k-0.jdUAPX h3")).getText());

        List<WebElement> textElements = element.findElements(By.cssSelector(".Text-sc-10o2fdq-0 .eizLtD"));
        if (textElements.size() > 0) {
            result.setLivingArea(textElements.get(0).getText());

            if (textElements.size() > 1) {
                result.setRoomCount(textElements.get(1).getText());
            }
        }

        List<WebElement> priceElements = element.findElements(By.cssSelector(".Text-sc-10o2fdq-0.fiVXiu"));
        if (priceElements.size() > 0) {
            result.setPrice(priceElements.get(0).getText());
        }

        result.setHref(element.findElement(By.cssSelector(".Box-wfmb7k-0.hkyQgZ a")).getAttribute("href"));

        result.setBuy(query.getBuy() == null ? "false" : query.getBuy().toString());
        result.setPostCode(query.getPostCode().toString());
        result.setProvider("Willhaben");

        return result;
    }
}
