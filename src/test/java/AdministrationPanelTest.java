import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class AdministrationPanelTest extends TestBase {
    private static final String LOGIN_NAME = "admin";
    private static final String PASSWORD = "admin";

    private static final String LOGOUT_LOCATOR = "//i[@class='fa fa-sign-out fa-lg']";
    private static final String BOX_APPS_MENU_LOCATOR = "//html//ul[@id='box-apps-menu']/li[*]";

    @Test
    private void testAdminPageLogin() {

        driver.navigate().to("http://localhost/litecart/admin/");
        wait.until(titleIs("My Store"));
        driver.findElement(By.name("username")).sendKeys(LOGIN_NAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        Assert.assertTrue(driver.findElement(By.xpath(LOGOUT_LOCATOR)).isDisplayed());
    }

    @Test
    private void testAdminPageBoxAppMenu() {
        testAdminPageLogin();
        int ulQuantity = driver.findElements(By.xpath(BOX_APPS_MENU_LOCATOR)).size();
        for (int i = 0; i < ulQuantity; i++) {

            System.out.println("\t" + driver.findElements(By.xpath(BOX_APPS_MENU_LOCATOR))
                    .get(i).getText());
            driver.findElements(By.xpath(BOX_APPS_MENU_LOCATOR))
                    .get(i)
                    .click();


            if (!driver.findElements(By.xpath("//ul[@class='docs']")).isEmpty()) {
                int innerUlQuantity = driver.findElements(By.xpath("//li[" + i + "]//a[*]")).size();
                for (int y = 0; y < innerUlQuantity; y++) {
                    System.out.println("\t\t" + driver.findElements(By.xpath("//li[" + i + "]//a[*]"))
                            .get(y).getText());

                    driver.findElements(By.xpath("//li[" + i + "]//a[*]"))
                            .get(y)
                            .click();
                }


            }
        }
    }
}
