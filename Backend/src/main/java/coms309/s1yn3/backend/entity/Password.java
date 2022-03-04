package coms309.s1yn3.backend.entity;

import javax.persistence.*;

@Entity
public class Password {
	@Id @Column(name = "user")
	int id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "user")
	User user;

	String password;

	public Password(User user, String password) {
		this.user = user;
		this.password = password;
	}

	public Password(int id, User user, String password) {
		this(user, password);
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
