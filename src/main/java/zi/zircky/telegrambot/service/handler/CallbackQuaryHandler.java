package zi.zircky.telegrambot.service.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import zi.zircky.telegrambot.service.managet.feedback.FeedbackManager;
import zi.zircky.telegrambot.service.managet.help.HelpManager;
import zi.zircky.telegrambot.service.managet.progress_control.ProgressControlManager;
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
  final ProgressControlManager progressControlManager;

  public CallbackQuaryHandler(HelpManager helpManager, FeedbackManager feedbackManager, TimetableManager timetableManager, TaskManager taskManager, ProgressControlManager progressControlManager) {
    this.helpManager = helpManager;
    this.feedbackManager = feedbackManager;
    this.timetableManager = timetableManager;
    this.taskManager = taskManager;
    this.progressControlManager = progressControlManager;
  }

  public BotApiMethod<?> answer(CallbackQuery callbackQuery, Bot bot) {
    String callbackData = callbackQuery.getData();
    String keyWord = callbackData.split("_")[0];
    if (TIMETABLE.equals(keyWord)) {
      return timetableManager.answerCallbackQuery(callbackQuery, bot);
    }
    if (TASK.equals(keyWord)) {
      return taskManager.answerCallbackQuery(callbackQuery, bot);
    }
    if (PROGRESS.equals(keyWord)) {
      return progressControlManager.answerCallbackQuery(callbackQuery, bot);
    }
    return switch (callbackData) {
      case FEEDBACK -> feedbackManager.answerCallbackQuery(callbackQuery, bot);
      case HELP -> helpManager.answerCallbackQuery(callbackQuery, bot);
      default -> null;
    };
  }
}
