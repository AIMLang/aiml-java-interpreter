import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.core.Bot;

public class Main {

    public static void main(String[] args) {
//        AIMLConst.setRootPath("./app-core");
        mainFunction(args);
    }

    private static void mainFunction(String[] args) {
        Bot bot = new Bot();
        Chat chat = new Chat(bot);
        chat.start();
    }
}