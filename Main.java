import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
    private static WebDriver driver;

    public static void loginF(WebDriver driver, String login, String pass){
        WebElement loginInput = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Login")));
        WebElement passInput = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Password")));
        loginInput.sendKeys(login);
        passInput.sendKeys(pass);
        loginInput.submit();
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "c:\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://m.mail.ru/login");
        while(true){
            loginF(driver,"origamycpa","123");
        }

    }
}
