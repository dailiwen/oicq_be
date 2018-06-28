package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理查询到的结果集
 * @author dailiwen
 * @date 2018/06/22
 */
public class ListMapHander implements ResultSetHander {
	@Override
	public List<Map<String, Object>> doHander(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
		ResultSetMetaData rsmd = resultSet.getMetaData();
        int cols = rsmd.getColumnCount();
        
        HashMap<String,Object> m = null;
        
        //遍历结果集
        while(resultSet.next()){
        	m = new HashMap<String,Object>();
        	//将结果集中的数据保存到HashMap中
        	for (int i = 1; i <= cols; i++) {
				m.put(rsmd.getColumnLabel(i), resultSet.getObject(i));
			}
        	resultList.add(m);
        }
		return resultList;
	}

}
