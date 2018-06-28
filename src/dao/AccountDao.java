package dao;

import entity.Account;
import util.EntityIDFactory;
import util.ListMapHander;
import util.ReflectionUtil;
import util.ResultSetHander;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dailiwen
 * @date 2018/06/22
 */
public class AccountDao extends BaseDao{
	private static AccountDao accountDao;
	ResultSetHander resultSetHander = new ListMapHander();

	public static AccountDao getAccountDao() {
		if (accountDao == null) {
			accountDao = new AccountDao();
		}
		return accountDao;
	}

	public Map<String, Object> login(Map<String, Object> map) throws Exception {
		String sql = ReflectionUtil.generateSelectSQL("oicq_account", map);
		List<Map<String, Object>> rs = resultSetHander.doHander(select(sql));
		if (rs != null && rs.size() != 0) {
			return rs.get(0);
		} else {
			return null;
		}
	}

	public Boolean register(Map<String, Object> map) throws Exception {
		Map<String, Object> loginName = new HashMap<>();
		loginName.put("login_name", map.get("login_name"));
		String selectSQL = ReflectionUtil.generateSelectSQL("oicq_account", loginName);
		List<Map<String, Object>> rs = resultSetHander.doHander(select(selectSQL));
		if (rs != null && rs.size() != 0) {
			return false;
		} else {
			Account account = new Account();
			account.setId(EntityIDFactory.createId());
			account.setLoginName((String) map.get("register_name"));
			account.setPassword((String) map.get("password"));
			account.setRegisterTime(getSqlDate());
			account.setStats(0);
			String insertSQL = ReflectionUtil.generateInsertSQL(account);
			execute(insertSQL);
			return true;
		}
	}

	public Boolean logout(String id) throws Exception {
		execute("UPDATE oicq_account SET stats = 0 WHERE id = '" + id + "'");
		return true;
	}
}
