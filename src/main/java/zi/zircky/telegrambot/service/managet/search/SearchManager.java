package zi.zircky.telegrambot.service.managet.search;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zi.zircky.telegrambot.entity.user.Action;
import zi.zircky.telegrambot.entity.user.Role;
import zi.zircky.telegrambot.entity.user.User;
import zi.zircky.telegrambot.repository.UserRepo;
import zi.zircky.telegrambot.service.factory.AnswerMethodFactory;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.SEARCH_CANCEL;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchManager extends AbstractManager {
  final AnswerMethodFactory methodFactory;
  final UserRepo userRepo;
  final KeyboardFactory keyboardFactory;

  @Autowired
  public SearchManager(AnswerMethodFactory methodFactory, UserRepo userRepo, KeyboardFactory keyboardFactory) {
    this.methodFactory = methodFactory;
    this.userRepo = userRepo;
    this.keyboardFactory = keyboardFactory;
  }

  @Override
  public BotApiMethod<?> answerCommand(Message message, Bot bot) {
    return askToken(message);
  }

  @Override
  public BotApiMethod<?> answerMessage(Message message, Bot bot) {
    try {
      bot.execute(methodFactory.getDeleteMessage(
          message.getChatId(),
          message.getMessageId() - 1
      ));
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
    var user = userRepo.findUserByChatId(message.getChatId());
    switch (user.getAction()) {
      case SENDING_TOKEN -> {
        return checkToken(message, user);
      }
    }
    return null;
  }



  @Override
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
    switch (callbackQuery.getData()) {
      case SEARCH_CANCEL -> {
        try {
          return cansel(callbackQuery, bot);
        } catch (TelegramApiException e) {
          log.error(e.getMessage());
        }
      }
    };
    return null;
  }

  private BotApiMethod<?> askToken(Message message) {
    Long chatId = message.getChatId();
    var user = userRepo.findUserByChatId(chatId);
    user.setAction(Action.SENDING_TOKEN);
    userRepo.save(user);
    return methodFactory.getSendMessage(
        chatId,
        "Отправте токен",
        keyboardFactory.getInlineKeyboard(
            List.of("Отмена операции"),
            List.of(1),
            List.of(SEARCH_CANCEL)
        )
    );
  }

  private BotApiMethod<?> cansel(CallbackQuery callbackQuery, Bot bot) throws TelegramApiException {
    Long chatId = callbackQuery.getFrom().getId();
    var user = userRepo.findUserByChatId(chatId);
    user.setAction(Action.FREE);
    userRepo.save(user);

    bot.execute(methodFactory.getAnswerCallbackQuery(
        callbackQuery.getId(),
        "Операция отменена успешно"
    ));

    return methodFactory.getDeleteMessage(
        chatId, callbackQuery.getMessage().getMessageId()
    );
  }

  private boolean validation(User userOne, User userTwo) {
    return userOne.getRole() != userTwo.getRole();
  }

  private BotApiMethod<?> checkToken(Message message, User user) {
    String token = message.getText();
    var userTwo = userRepo.findUserByToken(token);
    if (userTwo == null) {
      return methodFactory.getSendMessage(
          message.getChatId(),
          "По данному токену не найдено ни одного пользователя\n\nПовторите попытку! ",
          keyboardFactory.getInlineKeyboard(
              List.of("Отмена операции"),
              List.of(1),
              List.of(SEARCH_CANCEL)
          )
      );
    }
    if (validation(user, userTwo)) {
      if (user.getRole() == Role.TEACHER) {
        user.addUser(userTwo);
      } else {
        userTwo.addUser(user);
      }
      user.setAction(Action.FREE);
      userRepo.save(user);
      userRepo.save(userTwo);
      return methodFactory.getSendMessage(
          message.getChatId(),
          "Связь успешно установлена",
          null
      );
    }
    return methodFactory.getSendMessage(
        message.getChatId(),
        "Вы не можите установить соединение с учителем, если и вы им являетесь" +
            "или тоже самое, если вы ученик\n\nПовторите попытку! ",
        keyboardFactory.getInlineKeyboard(
            List.of("Отмена операции"),
            List.of(1),
            List.of(SEARCH_CANCEL)
        )
    );
  }
}
