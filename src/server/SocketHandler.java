package server;

import dao.AccountDao;
import dao.FriendDao;
import entity.Account;
import org.w3c.dom.Element;
import util.JacksonUtil;
import util.ReflectionUtil;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static server.Server.accounts;

/**
 * @author dailiwen
 * @date 2018/06/19
 */
public class SocketHandler implements Runnable{

	private static final String LOGIN = "login";
	private static final String REGISTER = "register";
	private static final String SEARCH_FRIEND = "search_friend";
	private static final String ADD_FRIEND = "add_friend";
	private static final String GET_FRIEND = "get_friend";
	private static final String SEND_MASSAGE = "send_massage";
	private static final String END_TALK = "end_talk";
	private static final String LOGOUT = "logout";

	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	private AccountDao accountDao = AccountDao.getAccountDao();
	private FriendDao friendDao = FriendDao.getFriendDao();

	public SocketHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
		this.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),true);
	}

	@Override
	public void run() {
		while (true) {
			try {
				boolean online = true;
				while (online) {
					String receiv = bufferedReader.readLine();
					if (receiv != null) {
						online = receive(JacksonUtil.jsonToMap(receiv), printWriter);
					} else {
						bufferedReader.close();
						this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
					}
				}
				printWriter.close();
				bufferedReader.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					bufferedReader.close();
					this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public boolean receive(Map<String, Object> map, PrintWriter printWriter) throws Exception {
		String type = null;
		Map<String, Object> result = new HashMap<>();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (key.equals("type")) {
				type = (String) map.get(key);
			} else {
				result.put(key, map.get(key));
			}
		}
		switch (type) {
			case LOGIN:
				return login(result, printWriter);
			case REGISTER:
				return register(result, printWriter);
			case GET_FRIEND:
				return getFriendList(result, printWriter);
			case SEARCH_FRIEND:
				return searchFriendList(result, printWriter);
			case ADD_FRIEND:
				return addFriend(result, printWriter);
			case SEND_MASSAGE:
				return sentMessage(result, (String) result.get("friend_id"));
			case LOGOUT:
				return logout(result, printWriter);
		}

		return true;
	}

	private boolean login(Map<String, Object> result, PrintWriter printWriter) throws Exception {
		Map<String, Object> map = accountDao.login(result);
		Account account = (Account) JacksonUtil.map2Object(map, Account.class);
		if (account != null) {
			accounts.put((String)account.getId(), account);
			account.setWriter(printWriter);
			//登录成功状态值为0
			accountDao.execute("UPDATE oicq_account SET stats = 1 WHERE id = '" + (String)account.getId() + "'");
			sentMessage(spliceMap(0, map), printWriter);
		} else {
			//登录失败状态值为1
			sentMessage(spliceMap(1, "登录失败"), printWriter);
		}

		return true;
	}

	private boolean register(Map<String, Object> result, PrintWriter printWriter) throws Exception {
		Boolean jug = accountDao.register(result);
		if (jug) {
			//注册成功状态值为0
			sentMessage(spliceMap(0, "注册成功"), printWriter);
		} else {
			//注册失败状态值为1
			sentMessage(spliceMap(1, "注册失败"), printWriter);
		}

		return true;
	}

	private boolean getFriendList(Map<String, Object> result, PrintWriter printWriter) throws Exception {
		List<Map<String, Object>> maps = friendDao.getFriendList(result);
		if (maps != null) {
			sentMessage(spliceMap(0, maps), printWriter);
		} else {
			sentMessage(spliceMap(1, "暂无好友"), printWriter);
		}
		return true;
	}

	private boolean searchFriendList(Map<String, Object> result, PrintWriter printWriter) throws Exception {
		List<Map<String, Object>> maps = friendDao.seachFriendList(result);
		if (maps != null) {
			sentMessage(spliceMap(0, maps), printWriter);
		} else {
			sentMessage(spliceMap(1, "无此人"), printWriter);
		}
		return true;
	}

	private boolean addFriend(Map<String, Object> result, PrintWriter printWriter) throws Exception {
		boolean jug = friendDao.addFriend(result);
		if (jug) {
			sentMessage(spliceMap(0, "添加成功"), printWriter);
		} else {
			sentMessage(spliceMap(1, "已经是好友"), printWriter);
		}
		return true;
	}

	public boolean sentMessage(Map<String, Object> result, String id) {
		Account account = accounts.get(id);
		if (account != null) {
			System.out.println(account.getLoginName());
			sentMessage(spliceMap(0, result), account.getWriter());
		}
		return true;
	}

	public boolean logout(Map<String, Object> result, PrintWriter printWriter) throws Exception {
		String id = (String) result.get("id");
		accountDao.logout(id);
		sentMessage(spliceMap(0, "退出登录"), printWriter);
		return true;
	}

	public String spliceMap(Integer type, Object object) {
		Map<String, Object> result = new HashMap<>();
		result.put("type", type);
		result.put("result", object);

		return JacksonUtil.objectToJson(result);
	}


	public void sentMessage(String message, PrintWriter writer) {
		System.out.println("server_sent" + message);
		try {
			writer.println(message);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
