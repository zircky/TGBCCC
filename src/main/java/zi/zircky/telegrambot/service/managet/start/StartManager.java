package zi.zircky.telegrambot.service.managet.start;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.service.factory.AnswerMethodFactory;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.FEEDBACK;
import static zi.zircky.telegrambot.service.data.CallbackData.HELP;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartManager extends AbstractManager {
  final AnswerMethodFactory methodFactory;
  final KeyboardFactory keyboardFactory;

  @Autowired
  public StartManager(AnswerMethodFactory methodFactory, KeyboardFactory keyboardFactory) {
    this.methodFactory = methodFactory;
    this.keyboardFactory = keyboardFactory;
  }

  @Override
  public SendMessage answerCommand(Message message, Bot bot) {
    return methodFactory.getSendMessage(
        message.getChatId(),
        """
              🖖Приветствую в Tutor-Bot, инструменте для упрощения взаимодействия репититора и ученика.
            
              Что бот умеет?
              📌 Составлять расписание
              📌 Прикреплять домашние задания
              📌 Ввести контроль успеваемости
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Help", "Feedback"),
            List.of(2),
            List.of(HELP, FEEDBACK)
        )
    );
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
