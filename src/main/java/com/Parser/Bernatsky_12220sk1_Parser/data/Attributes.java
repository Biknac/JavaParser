package com.Parser.Bernatsky_12220sk1_Parser.data;

import com.Parser.Bernatsky_12220sk1_Parser.handler.ExcelBuilder;
import com.Parser.Bernatsky_12220sk1_Parser.handler.Parser;
import com.Parser.Bernatsky_12220sk1_Parser.methods.get_set_property;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Attributes {

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("aboutTtitle", "ПАРСЕР СТВОРИВ");
        model.addAttribute("aboutName", "Бернацький Микита Дмитрович");
        model.addAttribute("aboutGroup", "(група: 122-20ск1)");
        return "main";
    }

    @GetMapping("/parser")
    public String parser() { return "parser";}

    @PostMapping("/injection")
    public ResponseEntity<ByteArrayResource> process(@RequestParam String name) throws InterruptedException, IOException {
        List<get_set_property> list = new ArrayList<>();

        if (name.isEmpty()){

            get_set_property empty = new get_set_property();

            empty.setName_product("empty_query_");

            empty.setSpecification("empty_query_");

            list.add(empty);
        }

        else {
            ChromeOptions chrome_options = new ChromeOptions();

            chrome_options.addArguments("--headless");

            chrome_options.addArguments("--remote-allow-origins=*");

            WebDriver chrome_driver = new ChromeDriver(chrome_options);

            Parser parser = new Parser();

            parser.setConnection(chrome_driver);

            list = parser.parse_request(name);

            parser.disableConnection(chrome_driver);
        }

        if (list.size() != 0) {
            ExcelBuilder excel_build = new ExcelBuilder();

            List<String> column = new ArrayList<>();
            column.add("ID");
            column.add("Назва");
            column.add("Опис");
            column.add("Ціна");
            column.add("Наявність");
            column.add("Посилання");

            excel_build.create_excel_column(column);
            excel_build.create_excel_list("ProductList from Rozetka", list);
        }

        String path = "./excel/" + "ProductList from Rozetka.xls";

        Path getPath = Paths.get(path);

        byte[] data = Files.readAllBytes(getPath);

        Thread.sleep(2000);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getPath.getFileName().toString())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
