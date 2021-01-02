package at.fhcampuswien.cyberpirates.WillBot.market.willhaben;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
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
import java.util.stream.Stream;

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
        // query parameter needed (price, size)

        List<Integer> helper = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            helper.add(i);
        }

        Map<Integer, WebDriver> drivers = new HashMap<>();
        List<String> allLinkText = helper.stream()
                .flatMap(i -> {
                    System.out.println("Page: " + i);

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless"); //keine Oberfläche nur der Code läuft im Hintergrund

                    drivers.put(i, new ChromeDriver(options));

                    drivers.get(i).get("https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote?sfId=3800c179-a691-4579-b182-97f95d41d407&isNavigation=true&page="+ i +"&rows=200"); //connection to the website
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    List<WebElement> cookieConsent = drivers.get(i).findElements(By.xpath("//*[@class='didomi-components-button didomi-button didomi-dismiss-button didomi-" +
                            "components-button--color didomi-button-highlight highlight-button']")); //close the cookies pop-up
                    cookieConsent.get(0).click();

                    scrollPage(drivers.get(i));

                    List<WebElement> elements = drivers.get(i).findElements(By.xpath("//div[@class='Box-wfmb7k-0 ResultListAdRowLayout___StyledBox-sc" +
                            "-1rmys2w-0 iRWwJP gnPcbh']/div[@class='Box-wfmb7k-0 hkyQgZ']/a"));

                    Stream<String> stream = elements.parallelStream().map(element -> element.getAttribute("href"));
                    return stream;
                })
                .collect(Collectors.toList());

        drivers.forEach((loop, driver) -> driver.close());

        allLinkText.forEach(System.out::println);
        System.out.println("Results: " + allLinkText.size());

        return new WillhabenResult();
    }

    private WillhabenQuery convert(Query query) {
        return null;
    }

    private Result convert(WillhabenResult query) {
        return null;
    }

    private void scrollPage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long pos = (Long) js.executeScript("return window.pageYOffset;");
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");


        while (pos + 1500 < height) {
            js.executeScript("window.scrollBy(0, 300)");
            pos = (Long) js.executeScript("return window.pageYOffset;");
            height = (Long) js.executeScript("return document.body.scrollHeight;");
        }
    }
}
