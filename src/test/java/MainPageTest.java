import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MainPageTest extends TestBase {

    private static final String CAMPAINS_PRODUCT_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']";
    private static final String MAIN_PAGE_PRODUCT_NAME_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']//div[@class='name']";
    private static final String MAIN_PAGE_PRICE_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']//div[@class='price-wrapper']//s[@class='regular-price']";
    private static final String MAIN_PAGE_DISCOUNT_PRICE_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']//div[@class='price-wrapper']//strong[@class='campaign-price']";

    private static final String DETAILS_PAGE_PRODUCT_NAME_LOCATOR = "//h1[@class='title']";
    private static final String DETAILS_PAGE_PRICE_LOCATOR = "//s[@class='regular-price']";
    private static final String DETAILS_PAGE_DISCOUNT_PRICE_LOCATOR = "//strong[@class='campaign-price']";

    @Test
    private void testMainPageProductSticker() {
        System.out.println("--- testMainPageProductSticker ---");
        String allProductsLocator = "li a[class='link']";
        String stickerLocator = "./div/div[contains(@class, 'sticker')]";

        driver.navigate().to(BASE_URL);
        wait.until(titleIs("Online Store | My Store"));

        List<WebElement> productBoxes = driver.findElements(By.cssSelector(allProductsLocator));

        productBoxes.forEach(e -> {
            List<WebElement> stickers = (e.findElements(By.xpath(stickerLocator)));
            System.out.println("For '" + e.getText() + "' stickers count = " + stickers.size() + "\n");
            Assert.assertEquals(1, stickers.size());
        });
    }

    @Test
    private void testProductDetails() {
        System.out.println("--- testProductDetails ---");
        driver.navigate().to(BASE_URL);
        wait.until(titleIs("Online Store | My Store"));

        Product mainPageDuck = new Product(
                driver.findElement(By.xpath(MAIN_PAGE_PRODUCT_NAME_LOCATOR)).getText(),
                driver.findElement(By.xpath(MAIN_PAGE_PRICE_LOCATOR)).getText(),
                driver.findElement(By.xpath(MAIN_PAGE_DISCOUNT_PRICE_LOCATOR)).getText()
        );

        System.out.println("mainPage: \n" + mainPageDuck);

        driver.findElement(By.xpath(CAMPAINS_PRODUCT_LOCATOR)).click();

        Product detailsPageDuck = new Product(
                driver.findElement(By.xpath(DETAILS_PAGE_PRODUCT_NAME_LOCATOR)).getText(),
                driver.findElement(By.xpath(DETAILS_PAGE_PRICE_LOCATOR)).getText(),
                driver.findElement(By.xpath(DETAILS_PAGE_DISCOUNT_PRICE_LOCATOR)).getText()
        );

        System.out.println("productDetailsPage: \n" + detailsPageDuck);

        Assert.assertEquals(mainPageDuck, detailsPageDuck);

    }

    @Test
    private void productDetailsElements() {
        System.out.println("--- mainPageProductBoxElements ---");
        driver.navigate().to(BASE_URL);
        wait.until(titleIs("Online Store | My Store"));

        verifyProductDetailsElements(MAIN_PAGE_PRICE_LOCATOR, MAIN_PAGE_DISCOUNT_PRICE_LOCATOR);

        driver.findElement(By.xpath(CAMPAINS_PRODUCT_LOCATOR)).click();

        verifyProductDetailsElements(DETAILS_PAGE_PRICE_LOCATOR, DETAILS_PAGE_DISCOUNT_PRICE_LOCATOR);

    }

    private void verifyProductDetailsElements(String regularPriceLocator, String discountPriceLocator) {

        String priceAttributes = driver.findElement(By.xpath(regularPriceLocator))
                .getCssValue("text-decoration");
        System.out.println(priceAttributes);

        Assert.assertTrue(priceAttributes.contains("line-through"));

        String colorRP = driver.findElement(By.xpath(regularPriceLocator)).getCssValue("color");
        String[] value = colorRP.replace("rgba(", "").replace(")", "").split(",");

        Assert.assertEquals(
                Integer.parseInt(value[0].trim()),
                Integer.parseInt(value[1].trim()),
                Integer.parseInt(value[2].trim()));

        String discountPriceAttributes = driver.findElement(By.xpath(discountPriceLocator))
                .getCssValue("font-weight");
        System.out.println(discountPriceAttributes);
        Assert.assertTrue(discountPriceAttributes.contains("700"));

        String colorDP = driver.findElement(By.xpath(discountPriceLocator))
                .getCssValue("color");
        value = colorDP.replace("rgba(", "").replace(")", "").split(",");

        Assert.assertEquals(
                0,
                Integer.parseInt(value[1].trim()),
                Integer.parseInt(value[2].trim()));

        double fontSizeRP = Double.parseDouble(driver.findElement(By.xpath(regularPriceLocator))
                .getCssValue("font-size").replace("px", ""));
        System.out.println(fontSizeRP);

        double fontSizeDP = Double.parseDouble(driver.findElement(By.xpath(discountPriceLocator))
                .getCssValue("font-size").replace("px", ""));
        System.out.println(fontSizeDP);

        Assert.assertTrue(fontSizeRP < fontSizeDP);
    }
}
