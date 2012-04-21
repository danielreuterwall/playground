package models;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.common.base.Objects;

@Entity
public class User extends Model {
	@Indexed(unique = true)
	public String mail;
	public String password;

	// FINDER
	public static final Finder<User> find = new Finder<User>(User.class);

	public User() {
	}

	public User(String mail, String password) {
		this.mail = mail;
		this.password = play.api.libs.Crypto.sign(password);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("mail", mail).toString();
	}

	public static User authenticate(String mail, String password) {
		List<User> user = find.field("mail").equal(mail).field("password")
				.equal(play.api.libs.Crypto.sign(password)).asList();
		return user.isEmpty() ? null : user.get(0);
	}
}
