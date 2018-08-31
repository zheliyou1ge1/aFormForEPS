package util;

import java.sql.ResultSet;
//结果集处理类
public interface ResultSetExtractor<T> {

    public abstract T extractData(ResultSet rs);

}