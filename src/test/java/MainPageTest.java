import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MainPageTest extends TestBase {

    @Test
    private void testMainPageProductSticker(){
        driver.navigate().to(BASE_URL);
        wait.until(titleIs("Online Store | My Store"));

        List<WebElement> productBoxes = driver.findElements(By.cssSelector("li a[class='link']"));

        productBoxes.forEach(element -> element.findElements(By.xpath("./div/div[contains(@class, 'sticker')]")));
        productBoxes.forEach(e -> {
            List<WebElement> stickers = (e.findElements(By.xpath("./div/div[contains(@class, 'sticker')]")));
            System.out.println("For '" + e.getText() + "' stickers count = " + stickers.size() + "\n");
            Assert.assertEquals(1, stickers.size());
        });

    }
}
