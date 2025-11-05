from selenium.webdriver.common.by import By
from .base_page import BasePage

class SalaryAdvicePage(BasePage):
    LINK_SALARY_ADVICE = (By.LINK_TEXT, "Salary Advice")
    CONTRACT_NO = (By.CSS_SELECTOR, ".contract-no")
    POST = (By.CSS_SELECTOR, ".post")
    NOTES = (By.CSS_SELECTOR, ".notes")
    SALARY_ITEM = (By.CSS_SELECTOR, ".salary-advice-item")

    def open_salary_advice(self):
        self.find(*self.LINK_SALARY_ADVICE).click()

    def get_contracts(self):
        return self.finds(*self.CONTRACT_NO)

    def get_posts(self):
        return self.finds(*self.POST)

    def open_contract(self, contract_no):
        self.find(By.LINK_TEXT, contract_no).click()

    def get_notes_text(self):
        return self.find(*self.NOTES).text
