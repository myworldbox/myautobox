package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class LoginPage(private val driver: WebDriver) {
    fun load(url: String) {
        driver.get(url)
    }

    fun login(username: String, password: String) {
        WebDriverWait(driver, Duration.ofSeconds(10)).until(
            presenceOfElementLocated(By.id("username"))
        )
        driver.findElement(By.id("username")).sendKeys(username)
        driver.findElement(By.id("password")).sendKeys(password)
        driver.findElement(By.id("kc-login")).click()
    }

}
