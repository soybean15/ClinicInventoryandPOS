/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;


import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
;

/**
 *
 * @author padilla
 */
public class ExportToExcel {

    private void renameFile(File file) {
        System.out.println("hello" + file.getAbsolutePath());
    }

    public void exportDataToCSV(String fileName, Object[][] data) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        if (!file.isFile()) {
            file.createNewFile();
        } else {
            renameFile(file);
        }
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file));

            int rowCount = data.length;

            for (int i = 0; i < rowCount; i++) {
                int columnCount = data[i].length;
                String[] values = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    values[j] = data[i][j] + "";
                }
                csvWriter.writeNext(values);
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {

        }

    }

   

}
