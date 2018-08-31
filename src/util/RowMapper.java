package util;
import java.sql.ResultSet;
import java.sql.SQLException;
//结果集行数据处理类
public interface RowMapper<T> {
    public abstract T mapRow(ResultSet rs, int index) throws SQLException;
}