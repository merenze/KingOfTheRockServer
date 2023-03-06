package coms309.s1yn3.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	@Fetch(FetchMode.JOIN)
	private List<GameUserRelation> gameRelations;

	/**
	 * The Lobby this player is in.
	 */
	@ManyToOne(targetEntity = Lobby.class)
	@JoinColumn(name = "lobby")
	@JsonBackReference
	private Lobby lobby;

	/**
	 * For use in UserRepository queries.
	 *
	 * @param id
	 * @param email
	 * @param username
	 * @param isAdmin
	 * @param lobby
	 */
	public User(int id, String email, String username, boolean isAdmin, Lobby lobby) {
		this(email, username, isAdmin);
		this.id = id;
		this.lobby = lobby;
	}

	public User(int id, String email, String username, boolean isAdmin) {
		this(email, username, isAdmin);
		this.id = id;
	}

	/**
	 * Create a new User.
	 *
	 * @param email    Unique email address for this User.
	 * @param username Unique username for this User.
	 * @param isAdmin  Admin status of this User.
	 */
	public User(String email, String username, boolean isAdmin) {
		this.email = email;
		this.username = username;
		this.isAdmin = isAdmin;
	}

	/**
	 * Empty constructor for use by JPA.
	 */
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

	public Lobby getLobby() {
		return lobby;
	}

	/**
	 * Do not set this to null.
	 * Instead, use disconnectFromLobby.
	 *
	 * @param lobby The Lobby in which this player waits.
	 */
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	/**
	 * @inheritDoc
	 */
	public String toString() {
		return username;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return id == user.id &&
				username.equals(user.username) &&
				email.equals(user.email) &&
				isAdmin == user.isAdmin;
	}
}
