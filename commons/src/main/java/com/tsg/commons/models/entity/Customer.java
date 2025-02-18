package com.tsg.commons.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE customer SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
@Table(name = "customer")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private Long idUser;
	
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
	
	@Column(name = "soft_delete")
	private Boolean softDelete = Boolean.FALSE;

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

}