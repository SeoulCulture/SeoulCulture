package seoul.culture.demo.datareader;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpotExcelToDatabaseMapper {
    int insert(SpotDto dto);
}