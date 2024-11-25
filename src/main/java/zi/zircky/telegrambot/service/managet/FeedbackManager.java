package zi.zircky.telegrambot.service.managet;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FeedbackManager {
  public BotApiMethod<?> answerCommand(Message message) {
    return SendMessage.builder()
        .chatId(message.getChatId())
        .text("""
              📍 Ссылки для обратной связи
              GitHub - https://github.com/zircky
              Telegram - https://t.me/zircky1
            """)
        .disableWebPagePreview(true)
        .build();
  }

  public BotApiMethod<?> answerCallbackQuary(CallbackQuery callbackQuery) {
    return EditMessageText.builder()
        .chatId(callbackQuery.getMessage().getChatId())
        .messageId(callbackQuery.getMessage().getMessageId())
        .text("""
              📍 Ссылки для обратной связи
              GitHub - https://github.com/zircky
              Telegram - https://t.me/zircky1
            """)
        .disableWebPagePreview(true)
        .build();
  }
}
