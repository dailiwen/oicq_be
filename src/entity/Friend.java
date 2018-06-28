package entity;

import annotation.Column;
import annotation.TableName;

import java.sql.Timestamp;


/**
 * @author dailiwen
 * @date 2018/06/22
 */
@TableName(tableName = "oicq_friend_list")
public class Friend {
	@Column(columnName = "id")
	private String id;
	@Column(columnName = "self_account_id")
	private String selfAccountId;
	@Column(columnName = "friend_account_id")
	private String friendAccountId;
	@Column(columnName = "become_friend_time")
	private Timestamp becomeFriendTime;

	public Friend() {
	}

	public Friend(String id, String selfAccountId, String friendAccountId, Timestamp becomeFriendTime) {
		this.id = id;
		this.selfAccountId = selfAccountId;
		this.friendAccountId = friendAccountId;
		this.becomeFriendTime = becomeFriendTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSelfAccountId() {
		return selfAccountId;
	}

	public void setSelfAccountId(String selfAccountId) {
		this.selfAccountId = selfAccountId;
	}

	public String getFriendAccountId() {
		return friendAccountId;
	}

	public void setFriendAccountId(String friendAccountId) {
		this.friendAccountId = friendAccountId;
	}

	public Timestamp getBecomeFriendTime() {
		return becomeFriendTime;
	}

	public void setBecomeFriendTime(Timestamp becomeFriendTime) {
		this.becomeFriendTime = becomeFriendTime;
	}
}
