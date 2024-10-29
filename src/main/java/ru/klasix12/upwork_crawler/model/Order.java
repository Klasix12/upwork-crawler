package ru.klasix12.upwork_crawler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private String title;
    private String posted;
    private String info;
    private String url;
}
