package zi.zircky.telegrambot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import zi.zircky.telegrambot.entity.user.User;
import zi.zircky.telegrambot.repository.UserRepo;
import zi.zircky.telegrambot.service.handler.CallbackQuaryHandler;
import zi.zircky.telegrambot.service.handler.CommandHandler;
import zi.zircky.telegrambot.service.handler.MessageHandler;
import zi.zircky.telegrambot.telegram.Bot;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UpdateDispatcher {
  final MessageHandler messageHandler;
  final CommandHandler commandHandler;
  final CallbackQuaryHandler callbackQueryHandler;

  @Autowired
  public UpdateDispatcher(MessageHandler messageHandler, CommandHandler commandHandler, CallbackQuaryHandler callbackQueryHandler) {
    this.messageHandler = messageHandler;
    this.commandHandler = commandHandler;
    this.callbackQueryHandler = callbackQueryHandler;
  }

  public BotApiMethod<?> distribute(Update update, Bot bot) {
    if (update.hasCallbackQuery()) {
      return callbackQueryHandler.answer(update.getCallbackQuery(), bot);
    }
    if (update.hasMessage()) {
      Message message = update.getMessage();
      if (message.hasText()) {
        if (message.getText().charAt(0) == '/'){
          return commandHandler.answer(message, bot);
        }
      }
      return messageHandler.answer(message, bot);
    }
    log.info("Unsupported update: " + update);
    return null;
  }
}
