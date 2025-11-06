package org.example.base

import config.Config
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

abstract class BaseCase {
    protected lateinit var driver: WebDriver
    protected lateinit var wait: WebDriverWait

    @BeforeEach
    fun setup() {
        val options = ChromeOptions()
        options.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080")
        driver = ChromeDriver(options)
        wait = WebDriverWait(driver, Duration.ofSeconds(10))
        driver.get(Config.BASE_URL)
    }

    @AfterEach
    fun teardown() {
        driver.quit()
    }

    fun load(url: String) {
        driver.get(url)
    }
}