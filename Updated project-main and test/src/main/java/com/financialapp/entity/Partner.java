package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PARTNER")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partnerId;

    private String name;
    private String apiKey;
	public static Object builder() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setId(Integer partnerId2) {
		// TODO Auto-generated method stub
		
	}
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
