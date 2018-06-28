package util;

import annotation.Column;
import annotation.TableName;
import entity.Friend;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 反射拼接sql工具类
 * @author dailiwen
 * @date 2018/06/22
 */
public class ReflectionUtil {

	public static String generateInsertSQL(Object entity) throws Exception, Exception {

		Class clazz = entity.getClass();

		Field[] fields = clazz.getDeclaredFields();
		TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(tableName.tableName());
		sql.append(" (");
		for(Field field: fields) {
			Column column = field.getAnnotation(Column.class);
			if(column == null || column.columnName().equals("writer")) {
				continue;
			}
			String columnName = column.columnName();
			sql.append(columnName).append(",");
		}
		sql.deleteCharAt(sql.length()-1);

		sql.append(") VALUES (");

		for(Field field:fields) {
			field.setAccessible(true);
			Object value = field.get(entity);
			String valueString ="";
			Column column = field.getAnnotation(Column.class);
			if(column == null || column.columnName().equals("writer")) {
				continue;
			}
			if(value instanceof String) {
				valueString = "'" + value + "'";
			} else 	if(value instanceof Timestamp) {
				valueString = "'" + value + "'";
			}
			else {
				valueString = value.toString();
			}
			sql.append(valueString).append(",");
		}

		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");

		return sql.toString();
	}

	public static String generateDeleteSQL(Object entity) throws Exception {
		Class clazz = entity.getClass();
		
		Method method = clazz.getDeclaredMethod("getId");
		method.setAccessible(true);
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(clazz.getSimpleName());
		sql.append(" WHERE id = ");
		sql.append("'" + method.invoke(entity) + "';");
		return sql.toString();
	}

	public static String generateUpdateSQL(Object entity) throws Exception {
		Class clazz = entity.getClass();
		String id = null;

		Field[] fields = clazz.getDeclaredFields();

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(clazz.getSimpleName());
		sql.append(" SET ");
		for(Field field: fields) {
			field.setAccessible(true);
			if("id".equals(field.getName())) {
				id = field.get(entity).toString();
				continue;
			}
			sql.append(field.getName()).append(" = ");
			Object value = field.get(entity);
			String valueString = "";
			if(value instanceof String) {
				valueString="'" + value + "'";
			} else {
				valueString = value.toString();
			}
			sql.append(valueString).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE id = '").append(id).append("'");

		return sql.toString();
	}

	public static String generateSelectSQL(Object entity) throws Exception {
		Class clazz = entity.getClass();
		TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(tableName.tableName());
		sql.append(" WHERE 1 = 1");

		return sql.toString();
	}

	public static String generateSelectSQL(Object entity, Map<String, Object> map) throws Exception {
		Class clazz = entity.getClass();
		TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(tableName.tableName());
		sql.append(" WHERE 1 = 1");
		Set<String> keys = map.keySet();
		for (String key: keys) {
			sql.append(" AND " + key + " = '" + map.get(key) + "'");
		}

		return sql.toString();
	}

	public static String generateSelectSQL(String tableName, Map<String, Object> map) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(tableName);
		sql.append(" WHERE 1 = 1");
		Set<String> keys = map.keySet();
		for (String key: keys) {
			sql.append(" AND " + key + " = '" + map.get(key) + "'");
		}

		return sql.toString();
	}
}
