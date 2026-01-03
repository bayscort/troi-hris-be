package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import com.project.troyoffice.util.UserContextUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shift_pattern_items")
@Getter
@Setter
@Audited
public class ShiftPatternItem extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_pattern_id", nullable = false)
    private ShiftPattern shiftPattern;

    // Urutan hari ke-berapa (1, 2, 3 ... sampai cycleDays)
    @Column(name = "day_sequence", nullable = false)
    private Integer daySequence;

    // Shift apa yang dipakai di hari urutan ini?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_master_id", nullable = false)
    private ShiftMaster shiftMaster;

}
