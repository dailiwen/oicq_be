package entity;

import annotation.Column;
import annotation.TableName;

import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * 账号实体类
 * @author dailiwen
 * @date 2018/06/22
 */
@TableName(tableName = "oicq_account")
public class Account {
	@Column(columnName = "id")
	private String id;
	@Column(columnName = "login_name")
	private String loginName;
	@Column(columnName = "password")
	private String password;
	@Column(columnName = "register_time")
	private Timestamp registerTime;
	@Column(columnName = "stats")
	private Integer stats;
	@Column(columnName = "writer")
	private PrintWriter writer;

	public Account() {
	}

	public Account(String id, String loginName, String password, Timestamp registerTime, Integer stats, PrintWriter writer) {
		this.id = id;
		this.loginName = loginName;
		this.password = password;
		this.registerTime = registerTime;
		this.stats = stats;
		this.writer = writer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public Integer getStats() {
		return stats;
	}

	public void setStats(Integer stats) {
		this.stats = stats;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
}
