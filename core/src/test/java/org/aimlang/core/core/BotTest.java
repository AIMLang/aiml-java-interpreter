package org.aimlang.core.core;

import org.aimlang.core.bot.BotImpl;
import org.aimlang.core.chat.ChatContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BotTest
 *
 * @author batiaev
 * @since 22/06/15
 */
public class BotTest {//FIXME
//    private BotImpl bot;
//    private BotRepository botRepository = new BotRepository(new InMemoryChatContextStorage());

//    @BeforeEach
//    public void setUp() {
//        bot = (BotImpl) botRepository.get("russian");
//        assertTrue(bot.wakeUp());
//    }

//    @Test
//    public void testMultisentenceRespond() {
//        var request = "Как дела?";
//        var correctResponds = List.of("отлично", "восхитительно", "замечательно", "прекрасно", "превосходно", "изумительно");
//        var respond = bot.multisentenceRespond(request, new ChatContext("Human")).trim();
//        assertTrue(correctResponds.contains(respond), "Request = " + request + ", Respond = " + respond);
//    }

//    @Test
//    public void testMultisentenceRespondWithRandom() {
//        var request = "Привет";
//        var respond = bot.multisentenceRespond(request, new ChatContext("Human")).trim();
//        var answers = "Здравствуй;Здравствуйте;Мое почтение!;Здарова;Приветствую;Привет;Доброго времени суток".split(";");
//        boolean result = false;
//        for (String answer : answers) {
//            if (respond.equals(answer)) {
//                result = true;
//                break;
//            }
//        }
//        assertTrue(result, "Request = " + request + ", Respond = " + respond);
//    }

//    @Test
//    public void testMultisentenceRespondWithSrai() {
//        String request = "Здравствуй";
//        String respond = bot.multisentenceRespond(request, new ChatContext("Human")).trim();
//        String[] answers = "Здравствуй;Здравствуйте;Мое почтение!;Здарова;Приветствую;Привет;Доброго времени суток".split(";");
//        boolean result = false;
//        for (String answer : answers) {
//            if (respond.equals(answer)) {
//                result = true;
//                break;
//            }
//        }
//        assertTrue(result, "Request = " + request + ", Respond = " + respond);
//    }
}
