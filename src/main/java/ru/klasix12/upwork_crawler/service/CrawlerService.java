package ru.klasix12.upwork_crawler.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Service;
import ru.klasix12.upwork_crawler.model.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlerService {
    private WebDriver driver = new FirefoxDriver();

    public List<Order> getOrdersByKeyWord(String keyWord) throws IOException {
        driver.get("https://www.upwork.com/nx/search/jobs/?q=" + keyWord + "&sort=recency");
        WebElement ordersElement = driver.findElement(By.className("card-list-container"));
        List<WebElement> orders = ordersElement.findElements(By.tagName("article"));

        List<Order> newOrders = new ArrayList<>();

        for (WebElement order : orders) {
            String posted = order.findElements(By.cssSelector("span[data-v-489be0f1='']")).get(1).getText();
            String title = order.findElement(By.className("up-n-link")).getText();
            StringBuilder sb = new StringBuilder();
            for (WebElement jobInfo : order.findElement(By.className("job-tile-info-list")).findElements(By.tagName("li"))) {
                sb.append(jobInfo.getText()).append(" ");
            }
            String orderUrl = order.findElement(By.className("up-n-link")).getAttribute("href");

            newOrders.add(Order.builder()
                    .title(title)
                    .info(sb.toString())
                    .posted(posted)
                    .url(orderUrl.substring(0, orderUrl.indexOf("/?referrer_url_path")))
                    .build());
        }
        return newOrders;
    }
}

