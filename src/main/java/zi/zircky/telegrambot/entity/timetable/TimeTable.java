package zi.zircky.telegrambot.entity.timetable;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timetable")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(name = "title")
  String title;

  @Column(name = "description")
  String description;

  @Enumerated(EnumType.STRING)
  WeekDay weekDay;

  @Column(name = "hour")
  Short hour;

  @Column(name = "minute")
  Short minute;
}
