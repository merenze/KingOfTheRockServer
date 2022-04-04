package coms309.s1yn3.backend.entity;

import coms309.s1yn3.backend.entity.relation.GameUserRelation;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
	/**
	 * Unique ID for this user.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Email address associated with this user.
	 */
	@Column(unique = true)
	private String email;

	/**
	 * Username associated with this user.
	 */
	@Column(unique = true)
	private String username;

	/**
	 * Admin status for this user.
	 */
	private boolean isAdmin;

	/**
	 * Games this user has played.
	 */
	@OneToMany(targetEntity = GameUserRelation.class, mappedBy = "user")
	private List<GameUserRelation> gameRelations;

	/**
	 * User constructor.
	 * @param id
	 * @param email
	 * @param username
	 * @param isAdmin
	 */
	public User(int id, String email, String username, boolean isAdmin) {
		this(email, username, isAdmin);
		this.id = id;
	}

	public User(String email, String username, boolean isAdmin) {
		this.email = email;
		this.username = username;
		this.isAdmin = isAdmin;
	}

	public User() {
	}

	/**
	 * @return Unique ID for this user.
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Email address associated with this user.
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Username associated with this user.
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Admin status for this user.
	 */
	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean admin) {
		isAdmin = admin;
	}

	public void patch(User user) {
		if (user.email != null) {
			this.email = user.email;
		}
		if (user.username != null) {
			this.username = user.username;
		}
	}

	public List<GameUserRelation> getGameRelations() {
		return gameRelations;
	}

	public void setGameRelations(List<GameUserRelation> gameRelations) {
		this.gameRelations = gameRelations;
	}
}
