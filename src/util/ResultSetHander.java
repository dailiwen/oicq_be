package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 处理查询到的结果集，调用接口
 * @author dailiwen
 * @date 2018/06/22
 */
public interface ResultSetHander {
	
	//自定义处理结果集
	public List<Map<String, Object>> doHander(ResultSet resultSet) throws SQLException;
	
}
