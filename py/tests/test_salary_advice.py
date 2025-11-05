from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from pages.login_page import LoginPage
from pages.salary_advice_page import SalaryAdvicePage
from config import USERS

def test_user1_check_version(driver):
    login = LoginPage(driver)
    login.load()
    login.login(USERS["user1"]["username"], USERS["user1"]["password"])
    WebDriverWait(driver, 5).until(
        EC.presence_of_element_located((By.XPATH, "//*[contains(text(), 'Version')]"))
    )
    assert "2.03r2" in driver.page_source

def test_user1_check_home_page(driver):
    login = LoginPage(driver)
    login.load()
    login.login(USERS["user1"]["username"], USERS["user1"]["password"])
    WebDriverWait(driver, 5).until(
        EC.presence_of_element_located((By.XPATH, "//*[contains(text(), 'Salary Advice')]"))
    )
    assert "Staff No." in driver.page_source
    assert "Staff Name" in driver.page_source

def test_user1_check_salary_advice_list(driver):
    login = LoginPage(driver)
    login.load()
    login.login(USERS["user1"]["username"], USERS["user1"]["password"])

    page = SalaryAdvicePage(driver)
    page.open_salary_advice()

    contracts = page.get_contracts()
    assert len(contracts) == 5

    for post in page.get_posts():
        assert post.text.strip() != ""

    assert "The system stores electronic files of salary advice slips from January 2021." in driver.page_source

def test_user1_check_first_contract(driver):
    login = LoginPage(driver)
    login.load()
    login.login(USERS["user1"]["username"], USERS["user1"]["password"])

    page = SalaryAdvicePage(driver)
    page.open_salary_advice()
    page.open_contract("000000-20-0033")

    assert page.find(*SalaryAdvicePage.POST).text.strip() != ""
    assert len(page.finds(*SalaryAdvicePage.SALARY_ITEM)) > 0

def test_user1_check_specific_contract(driver):
    login = LoginPage(driver)
    login.load()
    login.login(USERS["user1"]["username"], USERS["user1"]["password"])

    page = SalaryAdvicePage(driver)
    page.open_salary_advice()
    page.open_contract("000000-12-1232")

    assert page.find(*SalaryAdvicePage.POST).text.strip() != ""
    assert "No information yet." in driver.page_source
    assert page.get_notes_text().strip() == ""

def test_user2_check_no_access(driver):
    login = LoginPage(driver)
    login.load()
    login.login(USERS["user2"]["username"], USERS["user2"]["password"])
    assert "Access Denied" in driver.page_source
