package pl.coderslab.hotelpriceapp.scraper;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ItakaPriceScraper {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final ReentrantLock lock = new ReentrantLock(true);

    public static final class ScrapeResult {
        private final String name;
        private final BigDecimal price;

        public ScrapeResult(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }
        public String name() { return name; }
        public BigDecimal price() { return price; }
    }

    public ItakaPriceScraper() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions opts = new ChromeOptions()
                .setBinary("/usr/bin/chromium-browser")
                .addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1920,1080");
        this.driver = new ChromeDriver(opts);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public ScrapeResult scrapeNameAndLowestPrice(String url) throws IOException {
        lock.lock();
        try {
            driver.get(url);

            WebElement nameEl = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.mb-0.oui-lh-34"))
            );

            WebElement priceEl = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div[data-testid='current-price'] span[data-price-catalog-code]"))
            );

            String name = nameEl.getText();
            String digits = priceEl.getText().replaceAll("\\D+", "");
            BigDecimal price = digits.isEmpty() ? null : new BigDecimal(digits);

            return new ScrapeResult(name, price);
        } catch (Exception e) {
            throw new IOException("Scraping failed for URL: " + url, e);
        } finally {
            lock.unlock();
        }
    }

    @PreDestroy
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
