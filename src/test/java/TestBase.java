import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class TestBase {

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    public WebDriver driver;
    public WebDriverWait wait;
    public static final String BASE_URL = "http://localhost/litecart";

    @BeforeTest
    public void start() {
        if (tlDriver.get() != null) {
            driver = tlDriver.get();
            wait = new WebDriverWait(driver, 10);
            return;
        }

        driver = new ChromeDriver();
        tlDriver.set(driver);
        wait = new WebDriverWait(driver, 10);
        // driver.manage().window().maximize();
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    driver.quit();
                    driver = null;
                }));
    }

    @AfterTest
    public void stop() {
    }
}