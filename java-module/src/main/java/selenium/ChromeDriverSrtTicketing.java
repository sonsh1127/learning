package selenium;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class ChromeDriverSrtTicketing {

    private static final String SRT_LOGIN_PAGE_URL = "https://etk.srail.co.kr/cmc/01/selectLoginForm.do?pageId=TK0701000000";
    private static final String searchPage = "https://etk.srail.co.kr/hpg/hra/01/selectScheduleList.do?pageId=TK0101010000";
    private ChromeDriver driver;

    private long retryTimeDuration = 5000L;

    private long httpResponseWaitTime = 1000L;

    public ChromeDriverSrtTicketing() {
        this.driver = initChromeDriver();
    }

    /**
     * navigate to reservation page
     * fill search form
     * click search button
     * if reservation is available then tryReservation tickets and return
     */
    public void tryReservation(String from, String to, String date, String time) {

        navigate(searchPage);

        fillSearchForm(from, to, date, time);

        while (true) {
            clickSearchButton();
            waitFor(httpResponseWaitTime);
            if (reservationIsAvailable()) {
                reserve();
                return;
            }
            waitFor(retryTimeDuration);
        }
    }

    private void fillSearchForm(String departStation,
            String arriveStation, String date, String fromTime) {

        WebElement depart = driver.findElementById("dptRsStnCdNm");
        WebElement arrive = driver.findElementById("arvRsStnCdNm");
        depart.clear();
        arrive.clear();
        depart.sendKeys(departStation);
        arrive.sendKeys(arriveStation);

        driver.executeScript("document.getElementById('dptDt').value = arguments[0]", date);
        driver.executeScript("document.getElementById('dptTm').value = arguments[0]", fromTime);
    }

    private void clickSearchButton() {
        driver.executeScript(
                "document.getElementsByClassName('btn_large wx200 btn_burgundy_dark2 val_m corner inquery_btn')[0].click()"
        );
    }

    private boolean reservationIsAvailable() {
        List<WebElement> tableRows = driver.findElementsByTagName("tr");
        return tableRows.stream().anyMatch(
                tr -> findReserveButton(tr) != null
        );
    }

    private WebElement findReserveButton(WebElement parent) {
        try {
            WebElement generalSeatButton = parent.findElement(By.xpath("td[7]/a"));
            WebElement span = generalSeatButton.findElement(By.tagName("span"));
            if ("예약하기".equals(span.getText())) {
                return generalSeatButton;
            } else {
                return null;
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private void reserve() {
        WebElement resultElement = driver.findElementById("result-form");
        WebElement tbody = resultElement.findElement(By.tagName("tbody"));

        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        for (WebElement tr : tableRows) {
            WebElement button = findReserveButton(tr);
            if (button != null) {
                button.click();
                System.out.println("Reservation Button clicked!!");
                break;
            }
        }
    }

    private void navigate(String page) {
        this.driver.get(page);
    }

    public void login(String id, String password) {
        navigate(SRT_LOGIN_PAGE_URL);
        waitFor(httpResponseWaitTime);

        fillLoginForm(id, password);

        clickLoginButton();

        waitFor(httpResponseWaitTime);
    }

    private void fillLoginForm(String id, String password) {
        String idInputElementId = "srchDvNm01";
        String passwordElementId = "hmpgPwdCphd01";

        WebElement idInput = driver.findElementById(idInputElementId);
        WebElement pwdInput = driver.findElementById(passwordElementId);

        idInput.sendKeys(id);
        pwdInput.sendKeys(password);
    }

    private void clickLoginButton() {
        WebElement submit = driver.findElementByClassName("submit");
        submit.click();
    }

    private ChromeDriver initChromeDriver() {
        String exePath = "/Users/kakao/program/chromeDriver/chromedriver";
        System.setProperty("webdriver.chrome.driver", exePath);
        return new ChromeDriver();
    }

    private void waitFor(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException("thread interrupted");
        }
    }
}
