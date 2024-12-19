package zi.zircky.telegrambot.service.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import zi.zircky.telegrambot.repository.UserRepo;
import zi.zircky.telegrambot.service.managet.search.SearchManager;
import zi.zircky.telegrambot.telegram.Bot;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {
  final SearchManager searchManager;
  final UserRepo userRepo;

  @Autowired
  public MessageHandler(SearchManager searchManager, UserRepo userRepo) {
    this.searchManager = searchManager;
    this.userRepo = userRepo;
  }

  public BotApiMethod<?> answer(Message message, Bot bot) {
    var user = userRepo.findUserByChatId(message.getChatId());

    return switch (user.getAction()) {
      case SENDING_TOKEN -> searchManager.answerMessage(message, bot);
      default -> null;
    };
  }
}
