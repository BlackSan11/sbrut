import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Main {
    private static WebDriver driver;
    private static WebElement err;
    public static void loginF(WebDriver driver, String login, String pass,int iIter){
        WebElement loginInput = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Login")));
        WebElement passInput = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Password")));
        loginInput.sendKeys(login);
        loginInput.sendKeys(pass);
        loginInput.submit();
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "c:\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://m.mail.ru/login");

        int iIter = 0;

        while (true){
            loginF(driver,"orygamimy","KmUoP12",iIter);
            err = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className("error")));
            driver.get("https://m.mail.ru/login");
            iIter++;
        }


    }
}
