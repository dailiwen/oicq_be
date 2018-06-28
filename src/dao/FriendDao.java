package dao;

import entity.Friend;
import util.EntityIDFactory;
import util.ListMapHander;
import util.ReflectionUtil;
import util.ResultSetHander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dailiwen
 * @date 2018/06/22
 */
public class FriendDao extends BaseDao {
	private static FriendDao friendDao;
	ResultSetHander resultSetHander = new ListMapHander();

	public static FriendDao getFriendDao() {
		if (friendDao == null) {
			friendDao = new FriendDao();
		}
		return friendDao;
	}

	public List<Map<String, Object>> getFriendList(Map<String, Object> map) throws Exception {
		String sql = ReflectionUtil.generateSelectSQL("oicq_friend_list", map);
		List<Map<String, Object>> rs = resultSetHander.doHander(select(sql));
		List<Map<String, Object>> friendsInfo = new ArrayList<>();
		for (int i = 0; i < rs.size(); i++) {
			Map<String, Object> friendAccount = new HashMap<>();
			friendAccount.put("id", rs.get(i).get("friend_account_id"));
			String sql1 = ReflectionUtil.generateSelectSQL("oicq_account", friendAccount);
			Map<String, Object> friendInfo = resultSetHander.doHander(select(sql1)).get(0);
			friendsInfo.add(friendInfo);
		}
		if (rs != null && rs.size() != 0) {
			return friendsInfo;
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> seachFriendList(Map<String, Object> map) throws Exception {
		String sql = ReflectionUtil.generateSelectSQL("oicq_account", map);
		List<Map<String, Object>> rs = resultSetHander.doHander(select(sql));
		if (rs != null && rs.size() != 0) {
			return rs;
		} else {
			return null;
		}
	}

	public Boolean addFriend(Map<String, Object> map) throws Exception {
		String selectSQL = "SELECT * FROM oicq_friend_list WHERE oicq_friend_list.self_account_id = '" + (String) map.get("self_account_id") + "' AND oicq_friend_list.friend_account_id = '" + (String) map.get("friend_account_id") + "'";
		if (resultSetHander.doHander(select(selectSQL)) == null || resultSetHander.doHander(select(selectSQL)).size() == 0) {
			Friend friend = new Friend();
			friend.setId(EntityIDFactory.createId());
			friend.setSelfAccountId((String) map.get("self_account_id"));
			friend.setFriendAccountId((String) map.get("friend_account_id"));
			friend.setBecomeFriendTime(getSqlDate());
			String insertSQL = ReflectionUtil.generateInsertSQL(friend);
			execute(insertSQL);
			friend.setId(EntityIDFactory.createId());
			friend.setSelfAccountId((String) map.get("friend_account_id"));
			friend.setFriendAccountId((String) map.get("self_account_id"));
			friend.setBecomeFriendTime(getSqlDate());
			String insertSQL1 = ReflectionUtil.generateInsertSQL(friend);
			execute(insertSQL1);
			return true;
		} else {
			return false;
		}
	}
}
