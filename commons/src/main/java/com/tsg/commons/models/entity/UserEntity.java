package com.tsg.commons.models.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
@Table(name = "users", indexes = @Index(name = "unique_email", columnList = "email", unique = true))
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String username;

	private String password;


	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles=new ArrayList<>();

	@Column(name = "created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
	private LocalDateTime created_at;

	@Column(name = "updated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
	private LocalDateTime updated_at;

	@PrePersist
	public void prePersist() {
		created_at = LocalDateTime.now();
		updated_at = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		updated_at = LocalDateTime.now();
	}

	@Column(name = "soft_delete")
	private Boolean softDelete = Boolean.FALSE;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRoles().stream().map((Role role) -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return !this.softDelete;
	}

}
