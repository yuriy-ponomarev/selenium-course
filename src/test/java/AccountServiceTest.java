import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class AccountServiceTest extends TestBase {
    private static final String EMAIL_INPUT_LOCATOR = "//input[@name='email']";
    private static final String PASSWORD_INPUT_LOCATOR = "//input[@type='password']";
    private static final String LOGIN_BUTTON_LOCATOR = "//button[@name='login']";
    private static final String LOGOUT_USER_LOCATOR = "//a[contains(@href, '/logout')]";

    private static final String INPUT_FIRSTNAME_LOCATOR = "//input[@name='firstname']";
    private static final String INPUT_LASTNAME_LOCATOR = "//input[@name='lastname']";
    private static final String INPUT_ADDRESS1_LOCATOR = "//input[@name='address1']";
    private static final String INPUT_POSTCODE_LOCATOR = "//input[@name='postcode']";
    private static final String INPUT_CITY_LOCATOR = "//input[@name='city']";
    private static final String INPUT_EMAIL_LOCATOR = "//input[@name='email']";
    private static final String INPUT_PHONE_LOCATOR = "//input[@name='phone']";
    private static final String INPUT_PASSWORD_LOCATOR = "//input[@name='password']";
    private static final String INPUT_CONFIRMED_PASSWORD_LOCATOR = "//input[@name='confirmed_password']";
    private static final String SELECTOR_COUNTRY_LOCATOR = "//select[@name='country_code']";
    private static final String SELECTOR_ZONE_LOCATOR = "//select[@name='zone_code']";
    private static final String SUBMIT_BUTTON_LOCATOR = "//button[@type='submit']";

    @Test
    private void testUserLogin(String login, String password) {
        System.out.println("--- testUserLogin ---");
        driver.navigate().to(BASE_URL);
        driver.findElement(By.xpath(EMAIL_INPUT_LOCATOR)).clear();
        driver.findElement(By.xpath(EMAIL_INPUT_LOCATOR)).sendKeys(login);
        driver.findElement(By.xpath(PASSWORD_INPUT_LOCATOR)).clear();
        driver.findElement(By.xpath(PASSWORD_INPUT_LOCATOR)).sendKeys(password);
        driver.findElement(By.xpath(LOGIN_BUTTON_LOCATOR)).click();
        Assert.assertTrue(driver.findElement(By.xpath(LOGOUT_USER_LOCATOR)).isDisplayed());

    }

    @Test
    private void testUserLogout() {
        System.out.println("--- testUserLogout ---");
        driver.findElement(By.xpath(LOGOUT_USER_LOCATOR)).click();
        Assert.assertTrue(driver.findElement(By.xpath(LOGIN_BUTTON_LOCATOR)).isDisplayed());
    }


    @Test
    private void testCreateAccount() {
        System.out.println("--- testCreateAccount ---");
        String firsname = "John";
        String lastname = "Doe";
        String address1 = "West Side Str. 22/11";
        String postcode = "90210";
        String city = "San Diego";
        String phone = "+1902105545";
        String country = "United States";
        String zone = "California";

        String login = "user" + System.currentTimeMillis() + "@example.com";
        String password = "qwe123";
        System.out.println(login + " " + password);

        driver.navigate().to(BASE_URL + "/create_account");
        wait.until(titleIs("Create Account | My Store"));

        driver.findElement(By.xpath(INPUT_FIRSTNAME_LOCATOR)).sendKeys(firsname);
        driver.findElement(By.xpath(INPUT_LASTNAME_LOCATOR)).sendKeys(lastname);
        driver.findElement(By.xpath(INPUT_ADDRESS1_LOCATOR)).sendKeys(address1);
        driver.findElement(By.xpath(INPUT_POSTCODE_LOCATOR)).sendKeys(postcode);
        driver.findElement(By.xpath(INPUT_CITY_LOCATOR)).sendKeys(city);
        driver.findElement(By.xpath(INPUT_EMAIL_LOCATOR)).sendKeys(login);
        driver.findElement(By.xpath(INPUT_PHONE_LOCATOR)).clear();
        driver.findElement(By.xpath(INPUT_PHONE_LOCATOR)).sendKeys(phone);
        driver.findElement(By.xpath(INPUT_PASSWORD_LOCATOR)).sendKeys(password);
        driver.findElement(By.xpath(INPUT_CONFIRMED_PASSWORD_LOCATOR)).sendKeys(password);

        Select countrySelector = new Select(driver.findElement(By.xpath(SELECTOR_COUNTRY_LOCATOR)));
        countrySelector.selectByVisibleText(country);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Select zoneSelector = new Select(driver.findElement(By.xpath(SELECTOR_ZONE_LOCATOR)));
        zoneSelector.selectByVisibleText(zone);

        driver.findElement(By.xpath(SUBMIT_BUTTON_LOCATOR)).click();

        Assert.assertTrue(driver.findElement(By.xpath("//div[@class='notice success']")).isDisplayed());

        testUserLogout();
        testUserLogin(login, password);
        testUserLogout();
    }
}
