package zi.zircky.telegrambot.service.managet.progress_control;

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

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.PROGRESS;
import static zi.zircky.telegrambot.service.data.CallbackData.PROGRESS_STAT;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressControlManager extends AbstractManager {
  final AnswerMethodFactory methodFactory;
  final KeyboardFactory keyboardFactory;

  @Autowired
  public ProgressControlManager(AnswerMethodFactory methodFactory, KeyboardFactory keyboardFactory) {
    this.methodFactory = methodFactory;
    this.keyboardFactory = keyboardFactory;
  }

  @Override
  public BotApiMethod<?> answerCommand(Message message, Bot bot) {
    return mainMenu(message);
  }

  @Override
  public BotApiMethod<?> answerMessage(Message message, Bot bot) {
    return null;
  }

  @Override
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
    String callbackData = callbackQuery.getData();
    return switch (callbackData) {
      case PROGRESS -> mainMenu(callbackQuery);
      case PROGRESS_STAT -> stat(callbackQuery);
      default -> null;
    };
  }

  private BotApiMethod<?> mainMenu(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
              Здесь вы можете увидеть
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Статистика успеваемости"),
            List.of(1),
            List.of(PROGRESS_STAT)
        )
    );
  }

  private BotApiMethod<?> mainMenu(Message message) {
    return methodFactory.getSendMessage(
        message.getChatId(),
        """
              Здесь вы можете увидеть
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Статистика успеваемости"),
            List.of(1),
            List.of(PROGRESS_STAT)
        )
    );
  }

  private BotApiMethod<?> stat(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        "Здесь будет статистика",
        keyboardFactory.getInlineKeyboard(
            List.of("Назад"),
            List.of(1),
            List.of(PROGRESS)
        )
    );
  }
}
