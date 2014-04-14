package fr.oversimple.cryptmypass.model;

public class PasswordReference {

	private int id;
	private String description;
	private String login;
	private String password;

	public PasswordReference() {
	}

	public PasswordReference(int id, String description, String login,
			String password) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "PasswordReference [id=" + id + "description=" + description
				+ ", login=" + login + ", password=" + password + "]";
	}
}
