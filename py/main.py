from selenium import webdriver
from selenium.webdriver.common.by import By
import unittest
import time

BASE_URL = "https://apps.twghintranet.org/epdev/"

class SalaryAdviceTests(unittest.TestCase):

    def setUp(self):
        self.driver = webdriver.Chrome()
        self.driver.implicitly_wait(5)

    def tearDown(self):
        self.driver.quit()

    def login(self, username, password):
        driver = self.driver
        driver.get(BASE_URL)
        driver.find_element(By.ID, "username").send_keys(username)
        driver.find_element(By.ID, "password").send_keys(password)
        driver.find_element(By.ID, "kc-login").click()

    # -------------------------
    # User1: st518621
    # -------------------------

    def test_user1_test1_check_version(self):
        self.login("st518621", "A1234@test")
        self.assertIn("Version 2.03r2", self.driver.page_source)

    def test_user1_test2_check_home_page(self):
        self.login("st518621", "A1234@test")
        self.assertEqual("Salary Advice", self.driver.title)
        self.assertIn("Staff No.", self.driver.page_source)
        self.assertIn("Staff Name", self.driver.page_source)

    def test_user1_test3_check_salary_advice_list(self):
        self.login("st518621", "A1234@test")
        self.driver.find_element(By.LINK_TEXT, "Salary Advice").click()

        contracts = self.driver.find_elements(By.CSS_SELECTOR, ".contract-no")
        self.assertEqual(5, len(contracts))

        posts = self.driver.find_elements(By.CSS_SELECTOR, ".post")
        for post in posts:
            self.assertTrue(post.text.strip() != "")

        self.assertIn("Notes", self.driver.page_source)

    def test_user1_test4_check_first_contract(self):
        self.login("st518621", "A1234@test")
        self.driver.find_element(By.LINK_TEXT, "Salary Advice").click()

        self.driver.find_element(By.LINK_TEXT, "000000-20-0033").click()
        post_text = self.driver.find_element(By.CSS_SELECTOR, ".post").text
        self.assertTrue(post_text.strip() != "")

        salary_items = self.driver.find_elements(By.CSS_SELECTOR, ".salary-advice-item")
        self.assertGreater(len(salary_items), 0)

    def test_user1_test5_check_specific_contract(self):
        self.login("st518621", "A1234@test")
        self.driver.find_element(By.LINK_TEXT, "Salary Advice").click()

        self.driver.find_element(By.LINK_TEXT, "000000-12-1232").click()
        post_text = self.driver.find_element(By.CSS_SELECTOR, ".post").text
        self.assertTrue(post_text.strip() != "")

        self.assertIn("No information yet.", self.driver.page_source)
        notes_text = self.driver.find_element(By.CSS_SELECTOR, ".notes").text
        self.assertEqual(notes_text.strip(), "")

    # -------------------------
    # User2: st443043
    # -------------------------

    def test_user2_test1_check_no_access(self):
        self.login("st443043", "A1234@test")
        has_dialog = "No Access Permission" in self.driver.page_source
        self.assertTrue(has_dialog)


if __name__ == "__main__":
    unittest.main()
