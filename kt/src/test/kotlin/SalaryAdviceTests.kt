package test

import config.Config
import org.junit.jupiter.api.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait
import pages.LoginPage
import pages.SalaryAdvicePage
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SalaryAdviceTests {
    private lateinit var driver: WebDriver
    private lateinit var wait: WebDriverWait

    @BeforeAll
    fun setup() {
        val options = org.openqa.selenium.chrome.ChromeOptions()
        options.addArguments("--headless=new") // Use "--headless=new" for Chrome 109+
        options.addArguments("--disable-gpu")
        options.addArguments("--window-size=1920,1080")
        driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))
        wait = WebDriverWait(driver, Duration.ofSeconds(10))
    }

    @AfterAll
    fun teardown() {
        driver.quit()
    }

    @Test
    fun testUser1CheckVersion() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        val versionElement = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(normalize-space(.),='Version 2.03r2']"))
        )
        assertTrue(versionElement.text.contains("Version 2.03r2"))
    }

    @Test
    fun testUser1CheckHomePage() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        wait.until(titleIs("Salary Advice"))
        assertEquals("Salary Advice", driver.title)

        val staffNo = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(normalize-space(.), 'Staff No.')]"))
        )
        val staffName = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(normalize-space(.), 'Staff Name')]"))
        )

        assertTrue(staffNo.text.contains("Staff No."))
        assertTrue(staffName.text.contains("Staff Name"))
    }

    @Test
    fun testUser1CheckSalaryAdviceList() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        val page = SalaryAdvicePage(driver)
        page.openSalaryAdvice()

        val contracts = wait.until(
            presenceOfAllElementsLocatedBy(By.xpath("//td[contains(@class, 'contract-no')]"))
        )
        assertEquals(5, contracts.size)

        val posts = driver.findElements(By.xpath("//td[contains(@class, 'post')]"))
        posts.forEach { post -> assertFalse(post.text.trim().isEmpty()) }

        val notes = wait.until(
            presenceOfElementLocated(By.xpath("//*[normalize-space(.)='Notes']"))
        )
        assertEquals("Notes", notes.text.trim())
    }

    @Test
    fun testUser1CheckFirstContract() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        val page = SalaryAdvicePage(driver)
        page.openSalaryAdvice()
        page.openContract("000000-20-0033")

        val post = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(@class, 'post') and normalize-space(text()) != '']"))
        )
        assertFalse(post.text.trim().isEmpty())

        val salaryItems = wait.until(
            presenceOfAllElementsLocatedBy(By.xpath("//*[contains(@class, 'salary-advice-item')]"))
        )
        assertTrue(salaryItems.isNotEmpty())
    }

    @Test
    fun testUser1CheckSpecificContract() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        val page = SalaryAdvicePage(driver)
        page.openSalaryAdvice()
        page.openContract("000000-12-1232")

        val post = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(@class, 'post') and normalize-space(text()) != '']"))
        )
        assertFalse(post.text.trim().isEmpty())

        val noInfo = wait.until(
            presenceOfElementLocated(By.xpath("//*[normalize-space(.)='No information yet.']"))
        )
        assertEquals("No information yet.", noInfo.text.trim())

        val notes = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(@class, 'notes')]"))
        )
        assertEquals("", notes.text.trim())
    }

    @Test
    fun testUser2CheckNoAccess() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER2_USERNAME, Config.USER2_PASSWORD)

        val accessDenied = wait.until(
            presenceOfElementLocated(By.xpath("//*[normalize-space(.)='Access Denied']"))
        )
        assertEquals("Access Denied", accessDenied.text.trim())
    }
}
