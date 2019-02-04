package selenium;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;

public class ChromeDriverSrtTicketing {

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

        fillSearchForm(driver, from, to, date, time);

        while (true) {
            sendSearchRequest(driver);
            if (reservationIsAvailable()) {
                reserve(driver);
                return;
            }
            waitFor(retryTimeDuration);
        }
    }

    private void reserve(ChromeDriver driver) {
        WebElement resultElement = driver.findElementById("result-form");
        WebElement tbody = resultElement.findElement(By.tagName("tbody"));

        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        for (WebElement tr : tableRows) {
            WebElement button = findReserveButton(tr);
            if (button != null) {
                button.click();
                System.out.println("RESERVATION SUCCESS");
                break;
            }

        }
    }

    private boolean reservationIsAvailable() {
        List<WebElement> tableRows = driver.findElementsByTagName("tr");
        return tableRows.stream().anyMatch(
                tr -> findReserveButton(tr) != null
        );
    }

    private WebElement findReserveButton(WebElement parent) {
        try {
            WebElement button = parent.findElement(By.xpath("td[7]/a"));
            WebElement span = button.findElement(By.tagName("span"));
            System.out.println("#####################" + span.getText());
            if ("예약하기".equals(span.getText())) {
                return button;
            } else {
                return null;
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void sendSearchRequest(ChromeDriver driver) {
        //WebElement buttonParent = driver.findElementByClassName("btn_large");
        /*List<WebElement> input = buttonParent.findElements(By.tagName("input"));
        input.get(0).click();*/
        //buttonParent.click();

        driver.executeScript(
                "document.getElementsByClassName('btn_large wx200 btn_burgundy_dark2 val_m corner inquery_btn')[0].click()");
        //document.getElementsByClassName("aa bb");
        waitFor(2000);
    }

    private void fillSearchForm(ChromeDriver driver, String departStation,
            String arriveStation, String date, String fromTime) {

        String reservationPage = "https://etk.srail.co.kr/hpg/hra/01/selectScheduleList.do?pageId=TK0101010000";
        this.driver.get(reservationPage);

        WebElement depart = driver.findElementById("dptRsStnCdNm");
        WebElement arrive = driver.findElementById("arvRsStnCdNm");
        depart.clear();
        arrive.clear();
        depart.sendKeys(departStation);
        arrive.sendKeys(arriveStation);

        JavascriptExecutor js = driver;

        js.executeScript("document.getElementById('dptDt').value = arguments[0]", date);
        js.executeScript("document.getElementById('dptTm').value = arguments[0]", fromTime);

        /*WebElement dateElem = ((RemoteWebElement) searchForm).findElementByXPath(
                "//fieldset/div[@class=\"box1\"]/div/div/div[3]/div[1]/a/span[@class=\"ui-selectmenu-text\"]");

        WebElement timeElem = ((RemoteWebElement) searchForm).findElementByXPath(
                "//fieldset/div[@class=\"box1\"]/div/div/div[3]/div[2]/a/span[@class=\"ui-selectmenu-text\"]");*/

        //System.out.println(dateElem.getText());

        //System.out.println(timeElem.getText());
    }


    private void setSelected(WebElement option, boolean select) {
        boolean isSelected = option.isSelected();
        if ((!isSelected && select) || (isSelected && !select)) {
            option.click();
        }
    }

    public void selectByValue(String value, WebElement element) {
        List<WebElement> options = ((RemoteWebElement) element)
                .findElementsByXPath(".//option[@value=\"" + value + "\"]");

        boolean matched = false;
        for (WebElement option : options) {
            setSelected(option, true);

            matched = true;
        }

        if (!matched) {
            throw new NoSuchElementException("Cannot locate option with value: " + value);
        }
    }

    public void login(String id, String password) {
        String loginPage = "https://etk.srail.co.kr/cmc/01/selectLoginForm.do?pageId=TK0701000000";
        driver.get(loginPage);

        waitFor(httpResponseWaitTime);
        String srchDvNm01 = "srchDvNm01";
        WebElement idInput = driver.findElementById(srchDvNm01);
        idInput.sendKeys(id);
        String passwordElementId = "hmpgPwdCphd01";
        WebElement pwdInput = driver.findElementById(passwordElementId);
        pwdInput.sendKeys(password);
        WebElement submit = driver.findElementByClassName("submit");
        submit.click();
        waitFor(httpResponseWaitTime);
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
