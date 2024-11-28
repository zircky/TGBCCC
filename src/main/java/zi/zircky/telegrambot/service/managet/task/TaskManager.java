package zi.zircky.telegrambot.service.managet.task;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.service.factory.AnswerMethodFactory;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.*;
import static zi.zircky.telegrambot.service.data.CallbackData.TIMETABLE_ADD;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskManager extends AbstractManager {

  final AnswerMethodFactory methodFactory;
  final KeyboardFactory keyboardFactory;

  public TaskManager(AnswerMethodFactory methodFactory, KeyboardFactory keyboardFactory) {
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
      case TASK -> mainMenu(callbackQuery);
      case TASK_CREATE -> create(callbackQuery);
      default -> null;
    };
  }

  private BotApiMethod<?> mainMenu(Message message) {
    return methodFactory.getSendMessage(
        message.getChatId(),
        """
              🗂 Вы можете добавить домашнее задание вашему ученику
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Прикрепить домашнее задания"),
            List.of(1),
            List.of(TASK_CREATE)
        )
    );
  }

  private BotApiMethod<?> mainMenu(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
              🗂 Вы можете добавить домашнее задание вашему ученику
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Прикрепить домашнее задания"),
            List.of(1),
            List.of(TASK_CREATE)
        )
    );
  }

  private BotApiMethod<?> create(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
            👤 Выберете ученика, которому хотите дать домашнее задание
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Назад"),
            List.of(1),
            List.of(TASK)
        )
    );
  }

}
