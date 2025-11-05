package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class SalaryAdvicePage(private val driver: WebDriver) {
    fun openSalaryAdvice() {
        driver.findElement(By.linkText("Salary Advice")).click()
    }

    fun openContract(contractNo: String) {
        driver.findElement(By.linkText(contractNo)).click()
    }
}
