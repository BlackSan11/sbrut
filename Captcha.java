import net.marketer.RuCaptcha;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Captcha {
    private final String RUCAPTCHA_API_KEY = "f43320a0f3aaaab016f4ca15f0eb9847";
    private final int RUCAPTCHA_WAIT_RECOGNIZE = 2000; //время задержки перед запросом ответа(милисекунды)
    public WebDriver webDriver;


    public File getCapchaImg(WebElement element) throws IOException {
        // Делаем скриншот страницы
        File screen = ((TakesScreenshot) this.webDriver).getScreenshotAs(OutputType.FILE);
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

    public String recognizeCapcha(WebElement capcha) { //бомбим в рукапчу

        File imgFile = null;
        try {
            imgFile = getCapchaImg(capcha);
        } catch (IOException e) {
            System.out.println("Ошибка получения изображения капчи");
        }
        RuCaptcha.API_KEY = RUCAPTCHA_API_KEY;
        String CAPCHA_ID;
        String decryption;

        //отправляем нашу капчу в рукапчу
        String response = null;
        try {
            response = RuCaptcha.postCaptcha(imgFile);
            if (response.startsWith("OK")) { //если отправили без проблем

                CAPCHA_ID = response.substring(3); //получаем ID загруженной капчи

                while (true) {

                    response = RuCaptcha.getDecryption(CAPCHA_ID); //проверяем, разгаданна ли капча

                    if (response.equals(RuCaptcha.Responses.CAPCHA_NOT_READY.toString())) { //если капча еще не разгаданна
                        Thread.sleep(RUCAPTCHA_WAIT_RECOGNIZE); //уходим в сон
                        continue; // начинаем интерацию заново
                    } else if (response.startsWith("OK")) { //если разгадана
                        decryption = response.substring(3); //получаем разгаданное значение
                        //попытка ввода в вебформу
                        return response;
                        break; // заканчиваем цикл
                    } else {
                        System.out.println("Рукапча хуевничает");
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Ошибка отправки изображения капчи в RuCaptcha");
        }


    }

}
