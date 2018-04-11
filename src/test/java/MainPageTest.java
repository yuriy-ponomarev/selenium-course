import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.function.Function;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MainPageTest extends TestBase {

    private static final String CAMPAIGNS_PRODUCT_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']";
    private static final String MAIN_PAGE_PRODUCT_NAME_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']//div[@class='name']";
    private static final String MAIN_PAGE_PRICE_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']//div[@class='price-wrapper']//s[@class='regular-price']";
    private static final String MAIN_PAGE_DISCOUNT_PRICE_LOCATOR = "//div[@id='box-campaigns']//div[@class='content']//ul//li//a[@class='link']//div[@class='price-wrapper']//strong[@class='campaign-price']";
    private static final String ALL_PRODUCTS_LOCATOR = "product"; // for XPath: "//li[contains(@class, 'product')]"
    private static final String STICKER_LOCATOR = "sticker";

    private static final String DETAILS_PAGE_PRODUCT_NAME_LOCATOR = "//h1[@class='title']";
    private static final String DETAILS_PAGE_PRICE_LOCATOR = "//s[@class='regular-price']";
    private static final String DETAILS_PAGE_DISCOUNT_PRICE_LOCATOR = "//strong[@class='campaign-price']";

    private static final String ADD_TO_CART_BUTTON_LOCATOR = "add_cart_product";
    private static final String PRODUCT_SIZE_SELECTOR_LOCATOR = "[name='options[Size]']";

    private static final String CART_ITEMS_QUANTITY_LOCATOR = "span.quantity";
    private static final String CHECKOUT_LINK_LOCATOR = "//div[@id='cart']//a[3]";

    /* Cart */
    private static final String EMPTY_CART_MESSAGE_LOCATOR = "//html//p[1]//em";
    private static final String CART_REMOVE_PRODUCT_LOCATOR = "[name=remove_cart_item]";
    private static final String CART_FORM_LOCATOR = "[name =cart_form]";

    @Test
    private void testMainPageProductSticker() {
        System.out.println("--- testMainPageProductSticker ---");
        SoftAssert softAssert = new SoftAssert();

        driver.navigate().to(BASE_URL);
        wait.until(titleIs("Online Store | My Store"));

        List<WebElement> productBoxes = driver.findElements(By.className(ALL_PRODUCTS_LOCATOR));

        productBoxes.forEach(e -> {
            List<WebElement> stickers = (e.findElements(By.className(STICKER_LOCATOR)));
            System.out.println("For '" + e.getText() + "' stickers count = " + stickers.size() + "\n");
            softAssert.assertEquals(1, stickers.size());

        });

        softAssert.assertAll();
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

        driver.findElement(By.xpath(CAMPAIGNS_PRODUCT_LOCATOR)).click();

        Product detailsPageDuck = new Product(
                driver.findElement(By.xpath(DETAILS_PAGE_PRODUCT_NAME_LOCATOR)).getText(),
                driver.findElement(By.xpath(DETAILS_PAGE_PRICE_LOCATOR)).getText(),
                driver.findElement(By.xpath(DETAILS_PAGE_DISCOUNT_PRICE_LOCATOR)).getText()
        );

        System.out.println("productDetailsPage: \n" + detailsPageDuck);

        Assert.assertEquals(mainPageDuck, detailsPageDuck);

    }

    @Test
    private void testProductDetailsElements() {
        System.out.println("--- testProductDetailsElements ---");
        driver.navigate().to(BASE_URL);
        wait.until(titleIs("Online Store | My Store"));

        verifyProductDetailsElements(MAIN_PAGE_PRICE_LOCATOR, MAIN_PAGE_DISCOUNT_PRICE_LOCATOR);

        driver.findElement(By.xpath(CAMPAIGNS_PRODUCT_LOCATOR)).click();

        verifyProductDetailsElements(DETAILS_PAGE_PRICE_LOCATOR, DETAILS_PAGE_DISCOUNT_PRICE_LOCATOR);

    }

    @Test
    private void testCartAddProduct() {
        System.out.println("--- testCartAddProduct ---");

        for (int i=0; i<3; i++){
            driver.navigate().to(BASE_URL);
            addProductToCart();
        }

        driver.findElement(By.xpath(CHECKOUT_LINK_LOCATOR)).click();

        cartRemoveAll();

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

    private void addProductToCart(){
        driver.findElement(By.className(ALL_PRODUCTS_LOCATOR)).click();

        if (driver.findElements(By.cssSelector(PRODUCT_SIZE_SELECTOR_LOCATOR)).size() > 0) {
            new Select(driver.findElement(By.cssSelector(PRODUCT_SIZE_SELECTOR_LOCATOR))).selectByIndex(1);
        }
        int count = Integer.parseInt(driver.findElement(By.cssSelector(CART_ITEMS_QUANTITY_LOCATOR)).getText());
        driver.findElement(By.name(ADD_TO_CART_BUTTON_LOCATOR)).click();

        wait.until((WebDriver d) -> Integer.parseInt(driver
                .findElement(By.cssSelector(CART_ITEMS_QUANTITY_LOCATOR))
                .getText()) == count + 1);
    }

    private void cartRemoveAll(){
        while (driver.findElements(By.xpath(EMPTY_CART_MESSAGE_LOCATOR)).size() != 1){
            driver.findElement(By.cssSelector(CART_REMOVE_PRODUCT_LOCATOR)).click();
            Integer skuLines = driver.findElements(By.cssSelector(CART_FORM_LOCATOR)).size();
            wait.until((Function<WebDriver, Object>) driver -> driver
                    .findElements(By.cssSelector(CART_FORM_LOCATOR)).size() != skuLines ||
                    !driver.findElement(By.cssSelector(CART_FORM_LOCATOR)).isDisplayed());
        }
    }
}
