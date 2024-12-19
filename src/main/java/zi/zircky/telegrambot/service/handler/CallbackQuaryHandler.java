package zi.zircky.telegrambot.service.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zi.zircky.telegrambot.service.managet.auth.AuthManager;
import zi.zircky.telegrambot.service.managet.feedback.FeedbackManager;
import zi.zircky.telegrambot.service.managet.help.HelpManager;
import zi.zircky.telegrambot.service.managet.profile.ProfileManager;
import zi.zircky.telegrambot.service.managet.progress_control.ProgressControlManager;
import zi.zircky.telegrambot.service.managet.search.SearchManager;
import zi.zircky.telegrambot.service.managet.task.TaskManager;
import zi.zircky.telegrambot.service.managet.timetable.TimetableManager;
import zi.zircky.telegrambot.telegram.Bot;

import static zi.zircky.telegrambot.service.data.CallbackData.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQuaryHandler {
  final HelpManager helpManager;
  final FeedbackManager feedbackManager;
  final TimetableManager timetableManager;
  final TaskManager taskManager;
  final AuthManager authManager;
  final ProfileManager profileManager;
  final SearchManager searchManager;
  final ProgressControlManager progressControlManager;

  public CallbackQuaryHandler(HelpManager helpManager, FeedbackManager feedbackManager, TimetableManager timetableManager, TaskManager taskManager, AuthManager authManager, ProfileManager profileManager, SearchManager searchManager, ProgressControlManager progressControlManager) {
    this.helpManager = helpManager;
    this.feedbackManager = feedbackManager;
    this.timetableManager = timetableManager;
    this.taskManager = taskManager;
    this.authManager = authManager;
    this.profileManager = profileManager;
    this.searchManager = searchManager;
    this.progressControlManager = progressControlManager;
  }

  public BotApiMethod<?> answer(CallbackQuery callbackQuery, Bot bot) {
    String callbackData = callbackQuery.getData();
    String keyWord = callbackData.split("_")[0];
    return switch (keyWord) {
      case TIMETABLE -> timetableManager.answerCallbackQuery(callbackQuery, bot);
      case TASK -> taskManager.answerCallbackQuery(callbackQuery, bot);
      case PROGRESS -> progressControlManager.answerCallbackQuery(callbackQuery, bot);
      case AUTH -> authManager.answerCallbackQuery(callbackQuery, bot);
      case PROFILE -> profileManager.answerCallbackQuery(callbackQuery, bot);
      case SEARCH -> searchManager.answerCallbackQuery(callbackQuery, bot);
      default -> switch (callbackData) {
        case FEEDBACK -> feedbackManager.answerCallbackQuery(callbackQuery, bot);
        case HELP -> helpManager.answerCallbackQuery(callbackQuery, bot);
        default -> null;
      };
    };
  }
}
