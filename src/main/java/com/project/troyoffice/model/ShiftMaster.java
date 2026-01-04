package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "shift_masters")
@Audited
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftMaster extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, length = 50)
    private String code; // e.g., "S1", "OFF"

    @Column(nullable = false)
    private String name; // e.g., "Shift Pagi", "Day Off"

    @Column(name = "start_time")
    private LocalTime startTime; // 08:00

    @Column(name = "end_time")
    private LocalTime endTime;   // 17:00

    @Column(name = "is_cross_day")
    private boolean isCrossDay = false;

    @Column(name = "is_day_off")
    private boolean isDayOff = false;

    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes = 60;

    // --- CONFIGURABLE RULES ---

    @Column(name = "late_tolerance_minutes")
    private Integer lateToleranceMinutes = 0;

    @Column(name = "early_leave_tolerance_minutes")
    private Integer earlyLeaveToleranceMinutes = 0;

    @Column(name = "clock_in_window_minutes")
    private Integer clockInWindowMinutes = 60;
}
