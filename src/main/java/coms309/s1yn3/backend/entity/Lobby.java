package coms309.s1yn3.backend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lobby {
	/**
	 * The length of the join code.
	 */
	public static final int CODE_LENGTH = 4;

	/**
	 * Possible characters for the join code.
	 */
	public static final String CODE_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * The join code for this Lobby.
	 */
	@Id
	@Column(name = "code")
	private String code;

	/**
	 * The player hosting this Lobby.
	 */
	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "host")
	private User host;

	/**
	 * The players waiting in this Lobby.
	 */
	@OneToMany(targetEntity = User.class, mappedBy = "lobby", fetch = FetchType.EAGER)
	private List<User> players = new ArrayList<>();

	/**
	 * Empty constructor for use by JPA.
	 */
	public Lobby() {

	}

	/**
	 * Create a new lobby with the given code.
	 * Also used by JPA.
	 */
	public Lobby(String code) {
		this.code = code;
	}

	/**
	 * @return The join code for this Lobby.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * For use by JPA.
	 * Do not use.
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return The player hosting this Lobby.
	 */
	public User getHost() {
		return host;
	}

	/**
	 * @param host The player hosting this Lobby.
	 */
	public void setHost(User host) {
		this.host = host;
	}

	/**
	 * @return The players waiting in this lobby.
	 */
	public List<User> getPlayers() {
		return players;
	}

	/**
	 * For use by JPA.
	 * Do not use.
	 * @param players
	 */
	public void setPlayers(List<User> players) {
		this.players = players;
	}

	/**
	 * Add a User to this Lobby.
	 * @param user User to remove.
	 */
	public void addPlayer(User user) {
		user.setLobby(this);
		players.add(user);
	}

	/**
	 * Remove a User from this Lobby.
	 * @param user User to add.
	 */
	public void removePlayer(User user) {
		user.setLobby(null);
		players.remove(user);
	}

	/**
	 * @inheritDoc
	 */
	public String toString() {
		return String.format("LOBBY(code = %s, host = %s, players = %s)", code, host != null ? host.getUsername() : "null", players);
	}
}
