package zi.zircky.telegrambot.service.managet.timetable;

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

import static zi.zircky.telegrambot.service.data.CallbackData.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimetableManager extends AbstractManager {
  final AnswerMethodFactory methodFactory;
  final KeyboardFactory keyboardFactor;
  private final KeyboardFactory keyboardFactory;

  @Autowired
  public TimetableManager(AnswerMethodFactory methodFactory, KeyboardFactory keyboardFactor, KeyboardFactory keyboardFactory) {
    this.methodFactory = methodFactory;
    this.keyboardFactor = keyboardFactor;
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
      case TIMETABLE -> mainMenu(callbackQuery);
      case TIMETABLE_SHOW -> show(callbackQuery);
      case TIMETABLE_REMOVE -> remove(callbackQuery);
      case TIMETABLE_ADD -> add(callbackQuery);
      case TIMETABLE_1, TIMETABLE_2, TIMETABLE_3, TIMETABLE_4, TIMETABLE_5, TIMETABLE_6, TIMETABLE_7 -> showDay(callbackQuery);
      default -> null;
    };
  }



  private BotApiMethod<?> show(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
              📆 Выберете день недели
            """,
        keyboardFactory.getInlineKeyboard(
            List.of(
                "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье",
                "Назад"),
            List.of(7,1),
            List.of(
                TIMETABLE_1, TIMETABLE_2, TIMETABLE_3, TIMETABLE_4, TIMETABLE_5, TIMETABLE_6, TIMETABLE_7,
                TIMETABLE)
        )
    );
  }

  private BotApiMethod<?> add(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
              ✏️ Выберете день, в который хотите добавить занятие:
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Назад"),
            List.of(1),
            List.of(TIMETABLE)
        )
    );
  }

  private BotApiMethod<?> remove(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
              ✂️ Выберете занятие, которое хотите удалить из вашего расписания
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Назад"),
            List.of(1),
            List.of(TIMETABLE)
        )
    );
  }

  private BotApiMethod<?> mainMenu(Message message) {
    return methodFactory.getSendMessage(
        message.getChatId(),
        """
              📆 Здесь вы можете управлять вашим расписанием
            """,
        keyboardFactor.getInlineKeyboard(
            List.of("Показать мое расписание", "Удалить занятия", "Добавить занятия"),
            List.of(1, 2),
            List.of(TIMETABLE_SHOW, TIMETABLE_REMOVE, TIMETABLE_ADD)
        )
    );
  }

  private BotApiMethod<?> mainMenu(CallbackQuery callbackQuery) {
    return methodFactory.getEditeMessageText(
        callbackQuery,
        """
              📆 Здесь вы можете управлять вашим расписанием
            """,
        keyboardFactor.getInlineKeyboard(
            List.of("Показать мое расписание", "Удалить занятия", "Добавить занятия"),
            List.of(1, 2),
            List.of(TIMETABLE_SHOW, TIMETABLE_REMOVE, TIMETABLE_ADD)
        )
    );
  }

  private BotApiMethod<?> showDay(CallbackQuery callbackQuery) {

    return methodFactory.getEditeMessageText(
        callbackQuery,
        ""
    )
  }

}
