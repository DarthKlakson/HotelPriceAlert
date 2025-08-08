package pl.coderslab.hotelpriceapp.scraper;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class ItakaPriceScraper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ItakaPriceScraper() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions opts = new ChromeOptions()
                .setBinary("/usr/bin/chromium-browser")
                .addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1920,1080");
        this.driver = new ChromeDriver(opts);

        // dokładnie tak jak w TestRunnerze
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String scrapeName(String url) {
        driver.get(url);

        // czekaj na H1 (daj tu swój selektor – zwykle np. "h1.mb-0.oui-lh-34")
        WebElement nameEl = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("h1.mb-0.oui-lh-34"))
        );
        return nameEl.getText();
    }

    public BigDecimal scrapeLowestPrice(String url) {
        driver.get(url);

        // **tutaj ten sam selektor co w TestRunnerze**
        WebElement priceEl = wait.until(ExpectedConditions
                .visibilityOfElementLocated(
                        By.cssSelector("div[data-testid='current-price'] span[data-price-catalog-code]")
                )
        );

        // parsowanie "2 789 zł" → 2789
        String raw = priceEl.getText();
        String digits = raw.replaceAll("\\D+", "");
        return new BigDecimal(digits);
    }

    @PreDestroy
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
