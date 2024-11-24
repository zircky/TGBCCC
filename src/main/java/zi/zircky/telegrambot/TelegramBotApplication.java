package zi.zircky.telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import zi.zircky.telegrambot.telegram.TelegramProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(TelegramBotApplication.class, args);
  }

}