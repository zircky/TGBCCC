package zi.zircky.telegrambot.service.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.FeedbackManager;
import zi.zircky.telegrambot.service.managet.HelpManager;
import zi.zircky.telegrambot.telegram.Bot;

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.FEEDBACK;
import static zi.zircky.telegrambot.service.data.CallbackData.HELP;
import static zi.zircky.telegrambot.service.data.Command.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandHandler {
  final KeyboardFactory keyboardFactory;
  final FeedbackManager feedbackManager;
  final HelpManager helpManager;

  @Autowired
  public CommandHandler(KeyboardFactory keyboardFactory, FeedbackManager feedbackManager, HelpManager helpManager) {
    this.keyboardFactory = keyboardFactory;
    this.feedbackManager = feedbackManager;
    this.helpManager = helpManager;
  }

  public BotApiMethod<?> answer(Message message, Bot bot) {
    String command = message.getText();
    return switch (command) {
      case START -> start(message);
      case FEEDBACK_COMMAND -> feedbackManager.answerCommand(message);
      case HELP_COMMAND -> helpManager.answerCommand(message);
      default -> defaultAnswer(message);
    };
  }

  private BotApiMethod<?> defaultAnswer(Message message) {
    return SendMessage.builder()
        .chatId(message.getChatId())
        .text("Неподдерживаемая команда!")
        .build();
  }

  private BotApiMethod<?> start(Message message) {
    return SendMessage.builder()
        .chatId(message.getChatId())
        .replyMarkup(keyboardFactory.getInlineKeyboard(
            List.of("Help", "Feedback"),
            List.of(2),
            List.of(HELP, FEEDBACK)
        ))
        .text("""
              🖖Приветствую в Tutor-Bot, инструменте для упрощения взаимодействия репититора и ученика.
            
              Что бот умеет?
              📌 Составлять расписание
              📌 Прикреплять домашние задания
              📌 Ввести контроль успеваемости
            """)
        .build();
  }
}
