import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class AdministrationPanelTest extends TestBase {
    private static final String LOGIN_NAME = "admin";
    private static final String PASSWORD = "admin";

    private static final String LOGOUT_LOCATOR = "//i[@class='fa fa-sign-out fa-lg']";
    private static final String BOX_APPS_MENU_LOCATOR = "//html//ul[@id='box-apps-menu']/li[*]";

    @Test
    private void testAdminPageLogin() {

        driver.navigate().to(BASE_URL + "/admin/");
        wait.until(titleIs("My Store"));
        driver.findElement(By.name("username")).sendKeys(LOGIN_NAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        Assert.assertTrue(driver.findElement(By.xpath(LOGOUT_LOCATOR)).isDisplayed());
    }

    @Test
    private void testAdminPageLogout() {
        driver.findElement(By.xpath(LOGOUT_LOCATOR)).click();
    }

    @Test
    private void testAdminPageBoxAppMenu() {
        testAdminPageLogin();
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
}
