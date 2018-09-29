package cn.ioms.ssm.entity;

import java.util.Date;

public class User {
    private Long id;

    private Date createDate;

    private Date modifyDate;

    private String password;

    private String username;

    public User() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public User(Long id, String password, String username) {
		super();
		this.id = id;
		this.password = password;
		this.username = username;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}