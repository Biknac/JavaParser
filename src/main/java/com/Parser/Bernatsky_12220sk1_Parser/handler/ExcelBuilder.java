package com.Parser.Bernatsky_12220sk1_Parser.handler;

import com.Parser.Bernatsky_12220sk1_Parser.methods.get_set_property;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelBuilder {
    private HSSFWorkbook hssfwb;
    private HSSFSheet hssfs;
    public ExcelBuilder() {
        hssfwb = new HSSFWorkbook();

        hssfs = hssfwb.createSheet("Parser_for_Rozetka");
    }
    public HSSFWorkbook getHssfwb(){
        return hssfwb;
    }
    public HSSFSheet getHssfs(){
        return hssfs;
    }
    private void addRow(Row row, int numRow, String caps){
        row.createCell(numRow).setCellValue(caps);
    }
    public void create_excel_column(List<String> caps) {
        int numRow = 0;

        Row row = getHssfs().createRow(0);

        for (String capt : caps) {
            addRow(row, numRow, capt);
            numRow += 1;
        }
    }
    private void fill_list(List<get_set_property> list) {
        int row = 0;
        for (get_set_property data : list) {
            fill_file(hssfs, ++row, data);
        }
    }
    private static void fill_file(HSSFSheet sheet, int number_row, get_set_property data) {
        Row row = sheet.createRow(number_row);

        sheet.autoSizeColumn(number_row);

        row.createCell(0).setCellValue(data.getId_product());
        sheet.autoSizeColumn(0);

        row.createCell(1).setCellValue(data.getName_product());
        sheet.autoSizeColumn(1);

        row.createCell(2).setCellValue(data.getSpecification());
        sheet.autoSizeColumn(2);

        row.createCell(3).setCellValue(data.getPrice());
        sheet.autoSizeColumn(3);

        row.createCell(4).setCellValue(data.getAvailability());
        sheet.autoSizeColumn(4);

        row.createCell(5).setCellValue(data.getLink());
        sheet.autoSizeColumn(5);
    }
    public void create_excel_list(String name, List<get_set_property> list) {
        File path = new File("./excel");
        System.out.println("Path created: " + path);

        fill_list(list);


        try (FileOutputStream out = new FileOutputStream(path + "/" + name + ".xls")) {
            getHssfwb().write(out);
            System.out.println("File created: " + name);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.out.println("File dont created");
        }

        System.out.println("Processes completed");

    }
}
