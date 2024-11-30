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
import org.telegram.telegrambots.meta.api.objects.User;
import zi.zircky.telegrambot.entity.user.Action;
import zi.zircky.telegrambot.entity.user.Role;
import zi.zircky.telegrambot.entity.user.UserDetails;
import zi.zircky.telegrambot.repository.DetailsRepo;
import zi.zircky.telegrambot.repository.UserRepo;

import java.time.LocalDateTime;

@Aspect
@Order(10)
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationAspect {
  final UserRepo userRepo;
  final DetailsRepo detailsRepo;

  @Autowired
  public UserCreationAspect(UserRepo userRepo, DetailsRepo detailsRepo) {
    this.userRepo = userRepo;
    this.detailsRepo = detailsRepo;
  }

  @Pointcut("execution(* zi.zircky.telegrambot.service.UpdateDispatcher.distribute(..))")
  public void distributeMethodPointcut() {}

  @Around("distributeMethodPointcut()")
  public Object distributeMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    Update update = (Update) joinPoint.getArgs()[0];
    User telegramUser = null;

    if (update.hasMessage()) {
      telegramUser = update.getMessage().getFrom();
    } else if (update.hasCallbackQuery()) {
      telegramUser = update.getCallbackQuery().getFrom();
    } else {
      return joinPoint.proceed();
    }

    if (userRepo.existsById(telegramUser.getId())) {
      return joinPoint.proceed();
    }

    UserDetails details = UserDetails.builder()
        .firstName(telegramUser.getFirstName())
        .username(telegramUser.getUserName())
        .lastName(telegramUser.getLastName())
        .registeredAt(LocalDateTime.now())
        .build();

    detailsRepo.save(details);

    zi.zircky.telegrambot.entity.user.User newUser =
        zi.zircky.telegrambot.entity.user.User
            .builder()
            .chatId(telegramUser.getId())
            .action(Action.FREE)
            .role(Role.EMPTY)
            .details(details)
            .build();
    userRepo.save(newUser);
    return joinPoint.proceed();
  }

}
