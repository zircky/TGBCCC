package zi.zircky.telegrambot.service.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.service.managet.feedback.FeedbackManager;
import zi.zircky.telegrambot.service.managet.help.HelpManager;
import zi.zircky.telegrambot.service.managet.start.StartManager;
import zi.zircky.telegrambot.telegram.Bot;

import static zi.zircky.telegrambot.service.data.Command.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandHandler {
  final FeedbackManager feedbackManager;
  final HelpManager helpManager;
  final StartManager startManager;

  @Autowired
  public CommandHandler(FeedbackManager feedbackManager, HelpManager helpManager, StartManager startManager) {
    this.feedbackManager = feedbackManager;
    this.helpManager = helpManager;
    this.startManager = startManager;
  }

  public BotApiMethod<?> answer(Message message, Bot bot) {
    String command = message.getText();
    return switch (command) {
      case START -> startManager.answerCommand(message, bot);
      case FEEDBACK_COMMAND -> feedbackManager.answerCommand(message, bot);
      case HELP_COMMAND -> helpManager.answerCommand(message, bot);
      default -> defaultAnswer(message);
    };
  }

  private BotApiMethod<?> defaultAnswer(Message message) {
    return SendMessage.builder()
        .chatId(message.getChatId())
        .text("Неподдерживаемая команда!")
        .build();
  }

}
