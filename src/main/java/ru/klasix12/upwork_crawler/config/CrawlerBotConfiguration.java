package ru.klasix12.upwork_crawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.klasix12.upwork_crawler.bot.CrawlerBot;

@Configuration
public class CrawlerBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(CrawlerBot crawlerBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(crawlerBot);
        return api;
    }
}
