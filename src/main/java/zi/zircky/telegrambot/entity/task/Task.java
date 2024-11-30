package zi.zircky.telegrambot.entity.task;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(name = "title")
  String title;

  @Column(name = "text_content")
  String textContent;

  @Column(name = "actual_message_id")
  Integer messageId;
}
