package zi.zircky.telegrambot.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  @Column(name = "id")
  Long chatId;

  @Enumerated(EnumType.STRING)
  Role role;

  @Enumerated(EnumType.STRING)
  Action action;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_details_id")
  UserDetails details;
}
