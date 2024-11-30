package zi.zircky.telegrambot.proxy;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import zi.zircky.telegrambot.entity.user.Action;
import zi.zircky.telegrambot.entity.user.Role;
import zi.zircky.telegrambot.entity.user.User;
import zi.zircky.telegrambot.repository.UserRepo;
import zi.zircky.telegrambot.service.managet.auth.AuthManager;
import zi.zircky.telegrambot.telegram.Bot;

@Aspect
@Component
@Order(100)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthAspect {
  final UserRepo userRepo;
  final AuthManager authManager;

  @Autowired
  public AuthAspect(UserRepo userRepo, AuthManager authManager) {
    this.userRepo = userRepo;
    this.authManager = authManager;
  }

  @Pointcut("execution(* zi.zircky.telegrambot.service.UpdateDispatcher.distribute(..))")
  public void distributeMethodPointcut() {}

  @Around("distributeMethodPointcut()")
  public Object authMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    Update update = (Update) joinPoint.getArgs()[0];
    User user;
    if (update.hasMessage()) {
      user = userRepo.findById(update.getMessage().getChatId()).orElseThrow();
    } else if (update.hasCallbackQuery()) {
      user = userRepo.findById(update.getCallbackQuery().getMessage().getChatId()).orElseThrow();
    } else {
      return joinPoint.proceed();
    }

    if (user.getRole() != Role.EMPTY || user.getAction() == Action.AUTH) {
      return joinPoint.proceed();
    }

    return authManager.answerMessage(update.getMessage(), (Bot) joinPoint.getArgs()[1]);
  }
}
