import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class AdministrationPanelTest extends TestBase {
    private static final String LOGIN_NAME = "admin";
    private static final String PASSWORD = "admin";

    private static final String LOGOUT_LOCATOR = "//i[@class='fa fa-sign-out fa-lg']";

    @Test
    private void adminPageLogin() {

        driver.navigate().to("http://localhost/litecart/admin/");
        wait.until(titleIs("My Store"));
        driver.findElement(By.name("username")).sendKeys(LOGIN_NAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        Assert.assertTrue(driver.findElement(By.xpath(LOGOUT_LOCATOR)).isDisplayed());
    }
}
