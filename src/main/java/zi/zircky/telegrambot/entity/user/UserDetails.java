package zi.zircky.telegrambot.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetails {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(name = "username")
  String username;

  @Column(name = "first_name")
  String firstName;

  @Column(name = "last_name")
      String lastName;

  @Column(name = "registered_at")
  LocalDateTime registeredAt;

  @OneToOne
  @JoinColumn(name = "user_id")
  User user;
}
