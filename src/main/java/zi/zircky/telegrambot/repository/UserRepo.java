package zi.zircky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zi.zircky.telegrambot.entity.user.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
  User findUserByChatId(Long chatId);

  User findUserByToken(String token);
}
