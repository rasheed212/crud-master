package com.task.CRUD.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @NotBlank(message = "Mobile number cannot be empty")
    private String mobNum;

    @NotBlank(message = "PAN number cannot be empty")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN number format")
    private String panNum;

    private UUID managerId;

    @Builder.Default
    private boolean isActive = true;

    private String createdAt;

    private String updatedAt;

    public void setIsActive(boolean f){
        this.isActive = f;
    }
    
    public boolean getIsActive(){
        return this.isActive;
    }

}
