package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "appAdmin")
    private Boolean appAdmin;
    
    @Column(nullable = false)
    private Integer points = 0;

	public void setName(String string) {
		// TODO Auto-generated method stub
		
	}

    
}
