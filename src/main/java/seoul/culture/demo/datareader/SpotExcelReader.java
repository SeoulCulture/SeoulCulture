package seoul.culture.demo.datareader;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import seoul.culture.demo.entity.Spot;
import seoul.culture.demo.repository.SpotRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpotExcelReader implements ExcelReader {

    private final SpotExcelToDatabaseMapper excelDatabaseMapper;
    private final SpotRepository spotRepository;


    @Override
    public List<SpotDto> getResult(String path) {
        try {
            File excelFile = new File(path);
            FileInputStream inputStream = new FileInputStream(excelFile);
            OPCPackage opcPackage = OPCPackage.open(inputStream);
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row = sheet.getRow(i);
                if (null == row)
                    continue;

                List<String> spotInfo = new ArrayList<>();
                for (int col = 1; col <= 4; col++) {
                    if (row.getCell(col) == null) continue;
                    spotInfo.add(row.getCell(col).toString());
                }
                String sido = SpotFormatter.parseSido(row.getCell(0).toString());
                String spotName = SpotFormatter.parseSpotName(spotInfo);
                if (row.getCell(5) == null || row.getCell(6) == null) continue;
                double latitude = SpotFormatter.parseLocationPoint(row.getCell(5).toString());
                double longitude = SpotFormatter.parseLocationPoint(row.getCell(6).toString());

                if (spotRepository.existsBySidoAndSpotName(sido, spotName)) continue;
                excelDatabaseMapper.insert(SpotDto.of(spotName, sido, latitude, longitude));
            }
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Spot> all = spotRepository.findAll();
        return all.stream().map(Spot::toDto).collect(Collectors.toList());
    }
}