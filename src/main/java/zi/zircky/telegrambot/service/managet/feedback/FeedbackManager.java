package zi.zircky.telegrambot.service.managet.feedback;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.service.factory.AnswerMethodFactory;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackManager extends AbstractManager {

  final AnswerMethodFactory methodFactory;
  final KeyboardFactory keyboardFactory;

  @Autowired
  public FeedbackManager(AnswerMethodFactory methodFactory, KeyboardFactory keyboardFactory) {
    this.methodFactory = methodFactory;
    this.keyboardFactory = keyboardFactory;
  }

  @Override
  public BotApiMethod<?> answerCommand(Message message, Bot bot) {
    return methodFactory.getSendMessage(message.getChatId(),
            """
              📍 Ссылки для обратной связи
              GitHub - https://github.com/zircky
              Telegram - https://t.me/zircky1
            """,
            null);
  }

  @Override
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
            """
              📍 Ссылки для обратной связи
              GitHub - https://github.com/zircky
              Telegram - https://t.me/zircky1
            """,
        null);
  }

  @Override
  public BotApiMethod<?> answerMessage(Message message, Bot bot) {
    return null;
  }

}
