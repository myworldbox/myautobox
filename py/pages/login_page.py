from selenium.webdriver.common.by import By
from .base_page import BasePage
from config import BASE_URL

class LoginPage(BasePage):
    USERNAME = (By.ID, "username")
    PASSWORD = (By.ID, "password")
    LOGIN_BTN = (By.ID, "kc-login")

    def load(self):
        self.open(BASE_URL)

    def login(self, username, password):
        self.find(*self.USERNAME).send_keys(username)
        self.find(*self.PASSWORD).send_keys(password)
        self.find(*self.LOGIN_BTN).click()
