import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataForAuth {

    private String pathToPasswordsBase;

    public DataForAuth(String pathToPasswordsBase) {
        this.pathToPasswordsBase = pathToPasswordsBase;
    }

    public ArrayList<String> getPasswords() {
        ArrayList<String> passwordFromFile = new ArrayList<String>();
        try {
            Files.lines(Paths.get(this.pathToPasswordsBase), StandardCharsets.UTF_8).forEach(passwordFromFile::add);
        } catch (IOException e) {
            System.out.println("Не нашли файл с пасами");
            e.printStackTrace();
        }
    }

}
