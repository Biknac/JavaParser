package com.Parser.Bernatsky_12220sk1_Parser.handler;

import com.Parser.Bernatsky_12220sk1_Parser.methods.get_set_property;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class Parser {
    WebDriver driver;
    public WebDriver getConnection(){
        return driver;
    }
    public void setConnection(WebDriver drivers) { driver = drivers; }

    private WebDriver search(String data) throws InterruptedException {
        WebDriver connection_driver = getConnection();
        connection_driver.get("https://rozetka.com.ua/");

        WebElement input = connection_driver.findElement(By.cssSelector("input[name='search']"));
        WebElement button = connection_driver.findElement(By.cssSelector("button[class='button button_color_green button_size_medium search-form__submit ng-star-inserted']"));

        input.sendKeys(data);
        button.click();

        Thread.sleep(2000);

        return connection_driver;
    }
    private int count(WebDriver connection_driver) {
        List<WebElement> elems = connection_driver.findElements(By.className("pagination__item"));

        if (elems.size() == 0) {
            return 0;
        } else {
            return Integer.parseInt(elems.get(elems.size() - 1).getText());
        }
    }
    private List<get_set_property> data_processing(Elements adElems, List<get_set_property> property_list, String data) {
        for (Element product : adElems) {
            get_set_property found_data = new get_set_property();

            found_data.setName_product(data);

            found_data.setId_product(product.select("div.g-id.display-none").text());

            found_data.setSpecification(product.select("a.goods-tile__heading").text());

            String price = product.select("span.goods-tile__price-value").text();

            found_data.setPrice(price.isEmpty()? "На сайті ще не вказали ціну" : price);

            found_data.setAvailability(product.select("div.goods-tile__availability").text());

            found_data.setLink(product.select("a.goods-tile__heading").attr("href"));

            property_list.add(found_data);
        }

        return property_list;
    }
    private List<get_set_property> pages_info(List<get_set_property> information, WebDriver connection_driver, String data, int count) {
        while (true) {
            try {
                WebElement next_button = connection_driver.findElement(By.cssSelector("a.pagination__direction--forward"));

                if (next_button.getAttribute("class").contains("disabled")) {
                    break;
                }

                next_button.click();

                Thread.sleep(2000);

                Document document = Jsoup.parse(connection_driver.getPageSource());

                Elements element = document.select("div.goods-tile");

                Thread.sleep(2000);

                information = data_processing(element, information, data);


            } catch (Exception exc) {
                break;
            }
        }

        return information;
    }
    private void outputInformationToConsole(int count) {
        System.out.println("Starting processes");
        System.out.println("Pages with products found: " + (count == 0 ? 1 : count));
        System.out.println("Creating an excel file");
    }
    public void disableConnection(WebDriver connection_driver) {
        connection_driver.quit();
    }
    public List<get_set_property> parse_request(String data) throws InterruptedException {
        int num = 1, count;

        List<get_set_property> property = new ArrayList<>();

        WebDriver driver = search(data);

        Document document = Jsoup.parse(driver.getPageSource());

        Elements element = document.select("div.goods-tile");

        if (!element.isEmpty()) {
            count = count(driver);

            property = data_processing(element, property, data);

            if (count > 0) {
                property = pages_info(property, driver, data, count);
            }
        } else {
            get_set_property info = new get_set_property();

            info.setName_product(data);

            info.setSpecification("Запит не дав результатів");

            property.add(info);
        }

        return property;
    }
}