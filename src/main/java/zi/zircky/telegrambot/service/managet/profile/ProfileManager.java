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
import zi.zircky.telegrambot.service.managet.AbstractManager;
import zi.zircky.telegrambot.telegram.Bot;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileManager extends AbstractManager {
  final AnswerMethodFactory answerMethodFactory;
  final UserRepo userRepo;

  @Autowired
  public ProfileManager(AnswerMethodFactory answerMethodFactory, UserRepo userRepo) {
    this.answerMethodFactory = answerMethodFactory;
    this.userRepo = userRepo;
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
  public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) throws TelegramApiException {
    return null;
  }

  private BotApiMethod<?> showProfile(Message message) {
    Long chatId = message.getChatId();
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

    return answerMethodFactory.getSendMessage(
        chatId,
        text.toString(),
        null
    );
  }
}
