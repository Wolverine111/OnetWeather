import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class WeatherWidgetTest {

    public void AcceptCookies(){
        try {
            WebElement inputButton = $(By.className("cmp-button_button"));
            inputButton.click();
            inputButton.click();
        } catch  (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    public void settingLocationToKatowice(){
        $(By.name("locationSearch")).setValue("Katowice");
        WebElement dynamicElement = (new WebDriverWait(getWebDriver(),10)).until(ExpectedConditions.elementToBeClickable($("div.autocomplete-suggestions").$("div.autocomplete-suggestion")));
        dynamicElement.click();
        //$("div.autocomplete-suggestions").$("div.autocomplete-suggestion").waitUntil(visible, 2000).click();
    }

    @Before
    public void prepareSite(){
        open("https://pogoda.onet.pl");
        AcceptCookies();
        //WebDriverWait wait = new WebDriverWait(getWebDriver(),3);
        $(By.name("locationSearch")).waitUntil(visible,3000);
        settingLocationToKatowice();
    }


    @Test
    public void shouldCheckIfLocationIsKatowice() {
        //$(By.name("locationSearch")).setValue("Katowice");
        //$("div.autocomplete-suggestions").$("div.autocomplete-suggestion").click();
        $(".mainName ").shouldHave(text("Pogoda Katowice"));
    }

    @Test
    public void shouldCheckTemperatureInWidgetAndOnTodayDiv() {

        //$(By.name("locationSearch")).setValue("Katowice");
        //$("div.autocomplete-suggestions").$("div.autocomplete-suggestion").click();
        int temp = Integer.parseInt($(".temp").getText().substring(0,2));
        int temp2 = Integer.parseInt($(By.className("swiper-slide-active")).$("div.temp").getText().substring(0,2));
        Assert.assertTrue("Temperature "+ temp + " in main widget should be equal to temperature " + temp2 + " in actual time.", temp==temp2);
    }

    @Test
    public void shouldCheckAirQuality(){
        $(By.className("pollutionIconDesc")).shouldHave(text("DOBRA"));
    }

    @Test
    public void shouldCheckIfImageLoaded(){
        $(By.className("mood_1")).shouldBe(visible);
    }

    @Test
    public void shouldCheckIfLongterm(){
        $(By.className("tab_1")).click();
        $(By.className("boxTitle")).shouldHave(text("DŁUGOTERMINOWA"));
    }

    /* TODO pobrac wartosc cisnienia
    @Test
    public void shouldCheckIfPreasureIsHigh(){
        $(By.className("swiper-slide-active")).click();
        System.out.println($("span[class='restParamLabel']").getText());
        int preasure = Integer.parseInt($("span.restParamValue").getText().substring(0,3));
        Assert.assertTrue("Preasure is low", preasure > 1013);
    }*/

    @After
    public void closingSite(){
        closeWebDriver();
    }
}
