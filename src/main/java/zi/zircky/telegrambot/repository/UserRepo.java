package zi.zircky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zi.zircky.telegrambot.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
