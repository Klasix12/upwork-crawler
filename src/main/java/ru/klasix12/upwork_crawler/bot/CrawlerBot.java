package ru.klasix12.upwork_crawler.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.klasix12.upwork_crawler.model.Order;
import ru.klasix12.upwork_crawler.service.CrawlerService;

import java.io.IOException;
import java.util.List;

@Component
public class CrawlerBot extends TelegramLongPollingBot {
    private final String botUsername;
    private static final String PARSE_BY_KEY_WORD = "/parse_by_key_word";
    private final CrawlerService crawlerService;

    public CrawlerBot(@Value("${bot.token}") String botToken, @Value("${bot.username}") String botUsername, CrawlerService crawlerService) {
        super(botToken);
        this.botUsername = botUsername;
        this.crawlerService = crawlerService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String messageText = update.getMessage().getText();
        String chatId = String.valueOf(update.getMessage().getChatId());
        if (messageText.contains(PARSE_BY_KEY_WORD) && messageText.split(" ").length > 1) {
            try {
                List<Order> orders = crawlerService.getOrdersByKeyWord(messageText.split(" ")[1]);
                for (Order order : orders) {
                    sendMessage(chatId, String.format("Заказ: %s\nОпубликован: %s\nИнформация: %s\nСсылка: %s", order.getTitle(), order.getPosted(), order.getInfo(), order.getUrl()));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                // sendMessage(chatId, "Во время выполнения запроса произошла ошибка.\n" + e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
    private void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
