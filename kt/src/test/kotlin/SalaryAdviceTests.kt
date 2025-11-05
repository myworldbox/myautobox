package test

import config.Config
import org.junit.jupiter.api.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait
import pages.LoginPage
import pages.SalaryAdvicePage
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SalaryAdviceTests {
    private lateinit var driver: WebDriver
    private lateinit var wait: WebDriverWait

    @BeforeEach
    fun setup() {
        val options = org.openqa.selenium.chrome.ChromeOptions()
        options.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080")
        driver = ChromeDriver(options)
        wait = WebDriverWait(driver, Duration.ofSeconds(10))
        driver.get(Config.BASE_URL)
    }

    @AfterEach
    fun teardown() {
        driver.quit()
    }

    @Test
    @Order(1)
    fun testUser1CheckVersion() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        val version = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(normalize-space(.), 'Version')]"))
        )
        assertTrue(version.text.contains("2.03r2"))
    }

    @Test
    @Order(2)
    fun testUser1CheckHomePage() {
        val login = LoginPage(driver)
        login.load(Config.BASE_URL)
        login.login(Config.USER1_USERNAME, Config.USER1_PASSWORD)

        val title = wait.until(
            presenceOfElementLocated(By.xpath("//*[contains(normalize-space(.), 'Salary Advice')]"))
        )
        assertTrue(title.text.contains("Salary Advice"))

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
    @Order(3)
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
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
