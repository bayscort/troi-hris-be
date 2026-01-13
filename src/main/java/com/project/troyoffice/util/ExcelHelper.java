package com.project.troyoffice.util;

import com.project.troyoffice.dto.ManualScheduleDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    // Cek apakah file yang diupload benar-benar Excel
    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<ManualScheduleDto> excelToSchedulesMatrix(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            List<ManualScheduleDto> schedules = new ArrayList<>();
            DataFormatter formatter = new DataFormatter(); // Helper untuk baca cell sebagai String

            // 1. PARSE HEADER (ROW 0) untuk mendapatkan Peta Tanggal
            // Map: Index Kolom -> Tanggal
            Map<Integer, LocalDate> dateColumnMap = new HashMap<>();

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row is empty!");
            }

            // Loop mulai dari Kolom 1 (karena Kolom 0 adalah NIK)
            for (int cn = 1; cn < headerRow.getLastCellNum(); cn++) {
                Cell dateCell = headerRow.getCell(cn);

                if (dateCell != null) {
                    try {
                        LocalDate date;
                        // Cek apakah format cell di Excel benar-benar Date
                        if (DateUtil.isCellDateFormatted(dateCell)) {
                            date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                        } else {
                            // Fallback jika user menulis tanggal sebagai Text (misal "2026-01-01")
                            // Sesuaikan format jika perlu
                            date = LocalDate.parse(formatter.formatCellValue(dateCell));
                        }
                        dateColumnMap.put(cn, date);
                    } catch (Exception e) {
                        // Abaikan kolom jika header bukan tanggal valid (mungkin kolom keterangan lain)
                        System.out.println("Warning: Header kolom " + cn + " bukan tanggal valid.");
                    }
                }
            }

            // 2. PARSE DATA ROWS (Mulai Row 1)
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (isRowEmpty(row)) continue;

                // Ambil NIK (Kolom 0)
                Cell nikCell = row.getCell(0);
                String nik = formatter.formatCellValue(nikCell).trim();

                if (nik.isEmpty()) continue;

                // Loop Kolom Shift (Mulai Kolom 1)
                for (Map.Entry<Integer, LocalDate> entry : dateColumnMap.entrySet()) {
                    int colIndex = entry.getKey();
                    LocalDate date = entry.getValue();

                    Cell shiftCell = row.getCell(colIndex);
                    String shiftCode = formatter.formatCellValue(shiftCell).trim();

                    // Hanya buat DTO jika shift code TIDAK kosong
                    // (Cell kosong dianggap tidak ada perubahan jadwal)
                    if (!shiftCode.isEmpty()) {
                        ManualScheduleDto dto = new ManualScheduleDto();
                        dto.setEmployeeCode(nik);
                        dto.setDate(date);
                        dto.setShiftCode(shiftCode);

                        schedules.add(dto);
                    }
                }
            }

            return schedules;

        } catch (IOException e) {
            throw new RuntimeException("Gagal memparsing file Excel: " + e.getMessage());
        }
    }

    /**
     * Helper untuk mengambil nilai cell sebagai String dengan aman,
     * terlepas user mengetik angka atau teks di Excel.
     */
    private static String getCellValueAsString(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }
}
