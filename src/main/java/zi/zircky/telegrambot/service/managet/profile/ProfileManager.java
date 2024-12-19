package zi.zircky.telegrambot.service.managet.profile;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zi.zircky.telegrambot.repository.UserRepo;
import zi.zircky.telegrambot.service.factory.AnswerMethodFactory;
import zi.zircky.telegrambot.service.factory.KeyboardFactory;
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

import java.util.List;

import static zi.zircky.telegrambot.service.data.CallbackData.PROFILE_REFRESH_TOKEN;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileManager extends AbstractManager {
  final AnswerMethodFactory answerMethodFactory;
  final UserRepo userRepo;
  final KeyboardFactory keyboardFactory;

  @Autowired
  public ProfileManager(AnswerMethodFactory answerMethodFactory, UserRepo userRepo, KeyboardFactory keyboardFactory) {
    this.answerMethodFactory = answerMethodFactory;
    this.userRepo = userRepo;
    this.keyboardFactory = keyboardFactory;
  }

  @Override
  public BotApiMethod<?> answerCommand(Message message, Bot bot) {
    return showProfile(message);
  }

  @Override
  public BotApiMethod<?> answerMessage(Message message, Bot bot) {
    return null;
  }

  @Override
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
    return switch (callbackQuery.getData()) {
      case PROFILE_REFRESH_TOKEN -> refreshToken(callbackQuery);
      default -> null;
    };

  }

  private BotApiMethod<?> showProfile(Message message) {
    Long chatId = message.getChatId();
    return answerMethodFactory.getSendMessage(
        chatId,
        getProfileText(chatId),
        keyboardFactory.getInlineKeyboard(
            List.of("Обновить токен"),
            List.of(1),
            List.of(PROFILE_REFRESH_TOKEN)
        )
    );
  }

  private BotApiMethod<?> showProfile(CallbackQuery callbackQuery) {
    Long chatId = callbackQuery.getMessage().getChatId();
    return answerMethodFactory.getEditeMessageText(
        callbackQuery,
        getProfileText(chatId),
        keyboardFactory.getInlineKeyboard(
            List.of("Обновить токен"),
            List.of(1),
            List.of(PROFILE_REFRESH_TOKEN)
        )
    );
  }

  private String getProfileText(Long chatId) {
    StringBuilder text = new StringBuilder("\uD83D\uDC64 Профиль\n");
    var user = userRepo.findById(chatId).orElseThrow();
    var details = user.getDetails();

    if (details.getUsername() == null) {
      text.append("▪\uFE0FИмя пользователя - ").append(details.getUsername());
    } else {
      text.append("▪\uFE0FИмя пользователя - ").append(details.getFirstName());
    }
    text.append("\n▪\uFE0FРоль - ").append(user.getRole().name());
    text.append("\n▪\uFE0FВаш уникальный токен - \n").append(user.getToken().toString());
    text.append("\n\n⚠\uFE0F - токен необходим для того, чтобы ученик или преподаватель могли установиться между собой связь");
    return text.toString();
  }

  private BotApiMethod<?> refreshToken(CallbackQuery callbackQuery) {
    var user = userRepo.findUserByChatId(callbackQuery.getMessage().getChatId());
    user.refreshToken();
    userRepo.save(user);

    return showProfile(callbackQuery);
  }
}
