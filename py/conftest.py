import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options

@pytest.fixture
def driver():
    options = Options()
    options.add_argument("--headless=new")   # modern headless mode
    options.add_argument("--disable-gpu")    # optional, for Windows
    options.add_argument("--window-size=1920,1080")  # ensure consistent viewport
    driver = webdriver.Chrome(options=options)
    driver.implicitly_wait(5)
    yield driver
    driver.quit()
