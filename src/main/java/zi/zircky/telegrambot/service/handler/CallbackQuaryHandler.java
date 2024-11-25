package zi.zircky.telegrambot.service.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import zi.zircky.telegrambot.service.managet.FeedbackManager;
import zi.zircky.telegrambot.service.managet.HelpManager;
import zi.zircky.telegrambot.telegram.Bot;

import javax.security.auth.callback.CallbackHandler;

import static zi.zircky.telegrambot.service.data.CallbackData.FEEDBACK;
import static zi.zircky.telegrambot.service.data.CallbackData.HELP;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQuaryHandler {
  final HelpManager helpManager;
  final FeedbackManager feedbackManager;

  public CallbackQuaryHandler(HelpManager helpManager, FeedbackManager feedbackManager) {
    this.helpManager = helpManager;
    this.feedbackManager = feedbackManager;
  }

  public BotApiMethod<?> answer(CallbackQuery callbackQuery, Bot bot) {
    String callbackData = callbackQuery.getData();
    return switch (callbackData) {
      case FEEDBACK -> feedbackManager.answerCallbackQuary(callbackQuery);
      case HELP -> helpManager.answerCallbackQuery(callbackQuery);
      default -> null;
    };
  }
}
