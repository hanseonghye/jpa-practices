package me.kickscar.practices.jpa03.model02.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table( name = "User" )
public class User {

	@Id
	@Column(name = "no")
	@GeneratedValue( strategy = GenerationType.IDENTITY  )
	private Long no;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "email", nullable = false, length = 200)
	private String email;

	@Column(name = "password", nullable = false, length = 128)
	private String password;

	@Enumerated(EnumType.STRING)
	private GenderType gender;

	@Enumerated(EnumType.STRING)
	private RoleType role;

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" +
				"no=" + no +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", gender=" + gender +
				", role=" + role +
				'}';
	}
}
