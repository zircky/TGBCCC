package zi.zircky.telegrambot.service.managet.auth;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zi.zircky.telegrambot.entity.user.Action;
import zi.zircky.telegrambot.entity.user.Role;
import zi.zircky.telegrambot.repository.UserRepo;
import zi.zircky.telegrambot.service.factory.AnswerMethodFactory;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.AUTH_TEACHER;
import static zi.zircky.telegrambot.service.data.CallbackData.AUTH_STUDENT;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthManager extends AbstractManager {
  final UserRepo userRepo;
  final AnswerMethodFactory methodFactory;
  final KeyboardFactory keyboardFactory;

  @Autowired
  public AuthManager(UserRepo userRepo, AnswerMethodFactory methodFactory, KeyboardFactory keyboardFactory) {
    this.userRepo = userRepo;
    this.methodFactory = methodFactory;
    this.keyboardFactory = keyboardFactory;
  }

  @Override
  public BotApiMethod<?> answerCommand(Message message, Bot bot) {
    return null;
  }

  @Override
  public BotApiMethod<?> answerMessage(Message message, Bot bot) {
    Long chatId = message.getChatId();
    var user = userRepo.findById(chatId).orElseThrow();
    user.setAction(Action.AUTH);
    userRepo.save(user);


    return methodFactory.getSendMessage(
        chatId,
        """
              Выберете свою роль
            """,
        keyboardFactory.getInlineKeyboard(
            List.of("Ученик", "Учитель"),
            List.of(2),
            List.of(AUTH_STUDENT, AUTH_TEACHER)
        )
    );
  }

  @Override
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
    Long chatId = callbackQuery.getMessage().getChatId();
    Integer messageId = callbackQuery.getMessage().getMessageId();
    var user = userRepo.findById(chatId).orElseThrow();
    if (AUTH_STUDENT.equals(callbackQuery.getData())) {
      user.setRole(Role.TEACHER);
    } else {
      user.setRole(Role.STUDENT);
    }
    user.setAction(Action.FREE);
    userRepo.save(user);

    try {
      bot.execute(methodFactory.getDeleteMessage(
          chatId,
          messageId
      ));
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }

    return methodFactory.getAnswerCallbackQuery(
        callbackQuery.getId(),
        """
              Авторизация прошла успешна, повтарите предыдущий запрос!
            """
    );
  }
}
