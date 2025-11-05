package main

import (
	"fmt"
	"strings"
	"time"

	"github.com/tebeka/selenium"
)

const (
	seleniumPath = `C:\Program Files\Google\chrome-win\chrome.exe`
	port         = 9515
	baseURL      = "https://apps.twghintranet.org/epdev/"
)

func startWebDriver() (selenium.WebDriver, *selenium.Service, error) {
	opts := []selenium.ServiceOption{}
	service, err := selenium.NewChromeDriverService(seleniumPath, port, opts...)
	if err != nil {
		return nil, nil, fmt.Errorf("start ChromeDriver: %w", err)
	}

	caps := selenium.Capabilities{"browserName": "chrome"}
	chromeCaps := map[string]interface{}{
		"args": []string{
			// Point to your Chrome user data directory
			`--user-data-dir=C:\Users\VictorLeung\AppData\Local\Chromium\User Data`,
			// Pick a specific profile folder inside that directory
			`--profile-directory=Default`,
		},
	}
	caps["goog:chromeOptions"] = chromeCaps
	wd, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		service.Stop()
		return nil, nil, fmt.Errorf("connect to WebDriver: %w", err)
	}
	wd.SetImplicitWaitTimeout(10 * time.Second)
	return wd, service, nil
}

func login(wd selenium.WebDriver, username, password string) error {
	if err := wd.Get(baseURL); err != nil {
		return err
	}
	userField, err := wd.FindElement(selenium.ByID, "username")
	if err != nil {
		return err
	}
	passField, err := wd.FindElement(selenium.ByID, "password")
	if err != nil {
		return err
	}
	loginBtn, err := wd.FindElement(selenium.ByID, "kc-login")
	if err != nil {
		return err
	}
	userField.SendKeys(username)
	passField.SendKeys(password)
	loginBtn.Click()
	return nil
}

func runUser1CheckVersion() error {
	wd, service, err := startWebDriver()
	if err != nil {
		return err
	}
	defer func() {
		wd.Quit()
		if service != nil {
			service.Stop()
		}
	}()

	if err := login(wd, "st518621", "A1234@test"); err != nil {
		return err
	}

	source, err := wd.PageSource()
	if err != nil {
		return err
	}
	if !strings.Contains(source, "Version 2.03r2") {
		return fmt.Errorf("expected 'Version 2.03r2' not found in page source")
	}
	return nil
}

func main() {
	if err := runUser1CheckVersion(); err != nil {
		fmt.Printf("FAILED: %v\n", err)
	} else {
		fmt.Println("PASSED: Version check succeeded")
	}
}
