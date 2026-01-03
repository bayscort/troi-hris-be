package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import com.project.troyoffice.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "placements")
@Audited
@Getter
@Setter
public class Placement extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER) // Eager karena sering diakses saat login
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_site_id") // Nullable: Jika statusnya "Bench" atau WFH
    private ClientSite clientSite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_position_id") // Nullable: Jika statusnya "Bench" atau WFH
    private JobPosition jobPosition;

    // Jabatan spesifik di klien ini (Beda dengan jabatan internal)
    @Column(nullable = false)
    private String jobTitle; // Contoh: "Security Guard", "Java Developer"

    // Nomor Induk di Klien (Kadang klien minta ID card sendiri)
    private String employeeIdAtClient;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType; // PKWT, PKWTT, FREELANCE

    // Periode Kontrak
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate; // Null jika permanent

    // Gaji Pokok untuk kontrak ini (Base Payroll)
    private BigDecimal basicSalary;

    // Status Penempatan
    // ACTIVE = Sedang bekerja
    // COMPLETED = Kontrak habis
    // TERMINATED = Diberhentikan tengah jalan
    private Boolean isActive = true;

    // Helper method untuk cek kontrak valid
    public boolean isValidToday() {
        LocalDate today = LocalDate.now();
        return isActive &&
                (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (endDate == null || today.isBefore(endDate) || today.isEqual(endDate));
    }
}
