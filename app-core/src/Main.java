import com.batiaev.aiml.core.AIMLConst;
import com.batiaev.aiml.core.Bot;

public class Main {

    public static void main(String[] args) {
        AIMLConst.setRootPath();
        mainFunction(args);
    }

    private static void mainFunction(String[] args) {
        Bot bot = new Bot();
    }
}