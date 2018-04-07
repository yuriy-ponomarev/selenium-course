import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class AdministrationPanelTest extends TestBase {
    private static final String LOGIN_NAME = "admin";
    private static final String PASSWORD = "admin";

    private static final String COUNTRY_PAGE_URL = BASE_URL + "/admin/?app=countries&doc=countries";
    private static final String GEOZONES_URL = BASE_URL + "/admin/?app=geo_zones&doc=geo_zones";

    private static final String LOGOUT_LOCATOR = "//i[@class='fa fa-sign-out fa-lg']";
    private static final String COUNTRY_NAME_LOCATOR = "/html/body/div/div/div/table/tbody/tr/td/form/table/tbody/tr/td[5]/a[1]";
    private static final String COUNTRY_ZONE_LOCATOR = "/html/body/div/div/div/table/tbody/tr/td/form/table/tbody/tr/td[6]";

    @Test
    private void testAdminPageLogin() {
        System.out.println("--- testAdminPageLogin ---");
        driver.navigate().to(BASE_URL + "/admin/");
        wait.until(titleIs("My Store"));
        driver.findElement(By.name("username")).sendKeys(LOGIN_NAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        Assert.assertTrue(driver.findElement(By.xpath(LOGOUT_LOCATOR)).isDisplayed());
    }

    @Test
    private void testAdminPageLogout() {
        System.out.println("--- testAdminPageLogout ---");
        driver.findElement(By.xpath(LOGOUT_LOCATOR)).click();
        Assert.assertTrue(driver.findElement(By.name("login")).isDisplayed());
    }

    @Test
    private void testAdminPageBoxAppMenu() {
        testAdminPageLogin();
        System.out.println("--- testAdminPageBoxAppMenu ---");
        List<WebElement> liAppMenus = (driver.findElements(By.id("app-")));
        int liCount = liAppMenus.size();

        for (int i = 0; i < liCount; i++) {
            liAppMenus = (driver.findElements(By.id("app-")));
            liAppMenus.get(i).click();

            List<WebElement> liAppMenus2lvl = driver.findElements(By.xpath("//li[contains(@id, 'doc-')]"));
            int liCount2lvl = liAppMenus2lvl.size();

            for (int j = 0; j < liCount2lvl; j++) {
                liAppMenus2lvl = driver.findElements(By.xpath("//li[contains(@id, 'doc-')]"));
                liAppMenus2lvl.get(j).click();
                System.out.println(driver.findElement(By.xpath("//h1")).getText());
                Assert.assertTrue(driver.findElement(By.xpath("//h1")).isDisplayed());
            }
        }

        testAdminPageLogout();
    }

    @Test
    private void testCountriesSorting() {

        testAdminPageLogin();
        System.out.println("--- testCountriesSorting ---");
        driver.navigate().to(COUNTRY_PAGE_URL);

        verifyCountryNamesSorting(COUNTRY_NAME_LOCATOR);

        testAdminPageLogout();
    }

    @Test
    private void testCountryZonesSorting() {
        testAdminPageLogin();
        System.out.println("--- testCountriesSorting ---");
        driver.navigate().to(COUNTRY_PAGE_URL);
        String zonesCountryNameLocator = "//html//table[@id='table-zones']//tr/td[3]";

        List<WebElement> countries = driver.findElements(By.xpath(COUNTRY_NAME_LOCATOR));
        List<WebElement> zones = driver.findElements(By.xpath(COUNTRY_ZONE_LOCATOR));

        int countryCounter = zones.size();
        for (int i = 0; i < countryCounter; i++) {
            if (!zones.get(i).getText().equals("0")) {
                System.out.println("Verify zones for: " + countries.get(i).getText());
                countries.get(i).click();

                verifyCountryNamesSorting(zonesCountryNameLocator);

                driver.navigate().to(COUNTRY_PAGE_URL);
                countries = driver.findElements(By.xpath(COUNTRY_NAME_LOCATOR));
                zones = driver.findElements(By.xpath(COUNTRY_ZONE_LOCATOR));
            }
        }
        testAdminPageLogout();
    }

    @Test
    private void testGeoZonesSorting() {
        testAdminPageLogin();
        System.out.println("--- testGeoZonesSorting ---");
        driver.navigate().to(GEOZONES_URL);

        String countryLocator = "//tr[@class='row']//td[3]//a";
        String countryZoneLocator = "//select[contains(@name, '[zone_code]')]";

        List<WebElement> countries = driver.findElements(By.xpath(countryLocator));

        int countryCounter = countries.size();
        for (int i = 0; i < countryCounter; i++) {
            System.out.println("Verify zones for: " + countries.get(i).getText());
            countries.get(i).click();

            verifyCountryNamesSorting(countryZoneLocator);

            driver.navigate().to(GEOZONES_URL);
            countries = driver.findElements(By.xpath(countryLocator));
        }
        testAdminPageLogout();
    }


    private void verifyCountryNamesSorting(String locator) {
        List<String> countryNames = new ArrayList<>();
        driver.findElements(By.xpath(locator)).forEach(e -> countryNames.add(e.getText()));

        List<String> toBeSortedCountryNames = countryNames;
        java.util.Collections.sort(toBeSortedCountryNames);

        System.out.println("Actual: " + countryNames);
        System.out.println("Expected: " + toBeSortedCountryNames);

        Assert.assertEquals(countryNames, toBeSortedCountryNames);
    }
}
