package selenium;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SrtTicketing {

    public static void main(String[] args) throws Exception {
        ChromeDriver driver = initChromeDriver();

        //TODO pushing remote repository it must be empty string
        String id = "";
        String password = "";
        login(driver, id, password);
        tryReservation(driver);
    }

    /**
     * navigate to reservation page
     * fill search form
     * click search button
     *  if reservation is available then tryReservation tickets and return
     *
     *
     * @param driver
     * @throws InterruptedException
     */
    private static void tryReservation(ChromeDriver driver) throws InterruptedException {
        navigateToReservationPage(driver);

        fillSearchForm(driver);

        while (true) {
            sendSearchRequest(driver);
            Thread.sleep(800);
            if (reservationIsAvailable(driver)) {
                reserve(driver);
                return;
            }
            Thread.sleep(5000);
        }
    }

    private static void reserve(ChromeDriver driver) {
        List<WebElement> tableRows = driver.findElementsByTagName("tr");
        tableRows.forEach(
                tr -> {
                    WebElement button = findReserveButton(tr);
                    if (button != null) {
                        button.click();
                        return;
                    }
                }
        );
    }

    private static boolean reservationIsAvailable(ChromeDriver driver) {
        List<WebElement> tableRows = driver.findElementsByTagName("tr");
        return tableRows.stream().anyMatch(
                tr -> findReserveButton(tr) != null
        );
    }

    private static WebElement findReserveButton(WebElement parent) {
        try{
            WebElement economy = parent.findElement(By.xpath("td[7]"));
            return economy.findElement(By.className("button-02"));
        }catch(NoSuchElementException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void sendSearchRequest(ChromeDriver driver) {
        WebElement buttonParent = driver.findElementByClassName("button");
        List<WebElement> input = buttonParent.findElements(By.tagName("input"));
        input.get(0).click();
    }

    private static void fillSearchForm(ChromeDriver driver) {
        WebElement depart = driver.findElementById("dptRsStnCdNm");
        WebElement arrive = driver.findElementById("arvRsStnCdNm");
        depart.clear();
        arrive.clear();
        depart.sendKeys("수서");
        arrive.sendKeys("부산");

        WebElement time = driver.findElementByName("dptTm");
        time.sendKeys("14");
    }

    private static void navigateToReservationPage(ChromeDriver driver) {
        String reservationPage = "https://etk.srail.co.kr/hpg/hra/01/selectScheduleList.do?pageId=TK0101010000";

        driver.get(reservationPage);
    }

    private static void login(ChromeDriver driver, String id, String password) throws InterruptedException {
        driver.get("https://etk.srail.co.kr/cmc/01/selectLoginForm.do?pageId=TK0701000000");
        Thread.sleep(1000);
        String srchDvNm01 = "srchDvNm01";
        WebElement idInput = driver.findElementById(srchDvNm01);
        idInput.sendKeys(id);
        String pwd = "hmpgPwdCphd01";
        WebElement pwdInput = driver.findElementById(pwd);
        pwdInput.sendKeys(password);
        WebElement submit = driver.findElementByClassName("submit");
        submit.click();
        Thread.sleep(1000);
    }

    private static ChromeDriver initChromeDriver() {
        String exePath = "/Users/kakao/program/chromeDriver/chromedriver";
        System.setProperty("webdriver.chrome.driver", exePath);
        return new ChromeDriver();
    }
}
