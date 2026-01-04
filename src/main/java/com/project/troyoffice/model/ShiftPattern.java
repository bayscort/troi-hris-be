package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shift_patterns")
@Audited
@Getter
@Setter
public class ShiftPattern extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String name; // e.g., "Security 4 Grup (2P-2M-2L)"

    // Total hari dalam satu siklus.
    // Misal: 2 Pagi + 2 Malam + 2 Libur = 6 Hari cycle.
    @Column(name = "cycle_days", nullable = false)
    private Integer cycleDays;

    // Relasi ke item detail
    @OneToMany(mappedBy = "shiftPattern", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftPatternItem> items;
}
