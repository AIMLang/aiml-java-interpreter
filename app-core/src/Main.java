import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.core.AIMLConst;
import com.batiaev.aiml.core.AIMLTag;
import com.batiaev.aiml.core.Bot;
import com.batiaev.utils.IOUtils;

public class Main {

    public static void main(String[] args) {
//        AIMLConst.setRootPath("../../");
        mainFunction(args);
    }

    private static void mainFunction(String[] args) {
        Bot bot = new Bot();
        Chat chat = new Chat(bot);
        chat.start();
    }
}