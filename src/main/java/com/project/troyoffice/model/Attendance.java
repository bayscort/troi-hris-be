package com.project.troyoffice.model;

import com.project.troyoffice.enums.VerificationStatus;
import com.project.troyoffice.util.UserContextUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @Column(nullable = false)
    private Double checkInLatitude;

    @Column(nullable = false)
    private Double checkInLongitude;

    @Column(name = "check_in_photo_url", nullable = true)
    private String checkInPhotoUrl;

    private Double checkOutLatitude;

    private Double checkOutLongitude;

    @Column(name = "check_out_photo_url", nullable = true)
    private String checkOutPhotoUrl;

    private String location;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private Double totalHours;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        createdBy = UserContextUtil.getCurrentUsername();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updatedBy = UserContextUtil.getCurrentUsername();
    }

}
