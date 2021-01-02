package at.fhcampuswien.cyberpirates.WillBot.market.willhaben;

import at.fhcampuswien.cyberpirates.WillBot.market.IMarket;
import at.fhcampuswien.cyberpirates.WillBot.objects.Query;
import at.fhcampuswien.cyberpirates.WillBot.objects.Result;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;


public class WillhabenMarket implements IMarket {

    public static void main(String[] args) throws Exception {

        List<WebElement> searchPageResults;
        List<String> allLinkText = new ArrayList<>();

        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe"); //path at the local connection
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote?sfId=" +
                "3800c179-a691-4579-b182-97f95d41d407&isNavigation=true&page=1&rows=25"); //connection to the website
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//*[@class='didomi-components-button didomi-button didomi-dismiss-button didomi-" +
                "components-button--color didomi-button-highlight highlight-button']")).click(); //close the cookies pop-up

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Thread.sleep(500);
        js.executeScript("window.scrollBy(0,1000)","");
        js.executeScript("window.scrollBy(1000,1600)","");
        js.executeScript("window.scrollBy(1600,1800)","");
        js.executeScript("window.scrollBy(1800,2100)","");

        for (int i = 1; i <= 2; i++) { //iterate over pages

            if (i > 1) {
                driver.findElement(By.xpath("//a[@class='Button__ButtonContainer-sc-3uaafx-0 RVfyr Pagination__PaginationButton-zvrf30-1 Pagination__" +
                        "PaginationItemButton-zvrf30-2 iYAkvt dPQbTP' and text()='"+i+"']")).click();
                Thread.sleep(500);
                js.executeScript("window.scrollBy(0,1000)","");
                js.executeScript("window.scrollBy(1000,1600)","");
                js.executeScript("window.scrollBy(1600,1800)","");
                js.executeScript("window.scrollBy(1800,2100)","");
            }
            searchPageResults = driver.findElements(By.xpath("//div[@class='Box-wfmb7k-0 ResultListAdRowLayout___StyledBox-sc" +
                    "-1rmys2w-0 iRWwJP gnPcbh']/div[@class='Box-wfmb7k-0 hkyQgZ']/a"));

            for (WebElement link: searchPageResults) {
                allLinkText.add(link.getText());
            }
        }

        for (String eachLinkText : allLinkText){ //print out names of the immo
            System.out.println(eachLinkText);
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
