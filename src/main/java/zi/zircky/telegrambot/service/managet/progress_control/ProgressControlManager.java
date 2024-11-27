package zi.zircky.telegrambot.service.managet.progress_control;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressControlManager extends AbstractManager {
  @Override
  public BotApiMethod<?> answerCommand(Message message, Bot bot) {
    return null;
  }

  @Override
  public BotApiMethod<?> answerMessage(Message message, Bot bot) {
    return null;
  }

  @Override
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
    return null;
  }
}
