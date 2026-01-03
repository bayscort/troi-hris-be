package com.project.troyoffice.specification;

import com.project.troyoffice.enums.EducationLevel;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.EmployeeEducation;
import com.project.troyoffice.model.EmployeeJobReference;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EmployeeSpecification {

    public static Specification<Employee> nameLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("fullName")),
                    "%" + keyword.toLowerCase() + "%"
            );
        };
    }

//    public static Specification<Employee> hasJobReference(UUID jobReferenceId) {
//        return (root, query, cb) -> {
//            if (jobReferenceId == null) {
//                return cb.conjunction();
//            }
//
//            Join<Employee, EmployeeJobReference> jobJoin =
//                    root.join("jobReferences", JoinType.LEFT);
//
//            return cb.equal(
//                    jobJoin.get("jobReference").get("id"),
//                    jobReferenceId
//            );
//        };
//    }

    public static Specification<Employee> hasAnyJobReference(List<UUID> jobReferenceIds) {
        return (root, query, cb) -> {

            if (jobReferenceIds == null || jobReferenceIds.isEmpty()) {
                return cb.conjunction();
            }

            Join<Employee, EmployeeJobReference> jobJoin =
                    root.join("jobReferences", JoinType.LEFT);

            return jobJoin
                    .get("jobReference")
                    .get("id")
                    .in(jobReferenceIds);
        };
    }


//    public static Specification<Employee> hasEducationLevel(EducationLevel level) {
//        return (root, query, cb) -> {
//            if (level == null) {
//                return cb.conjunction();
//            }
//
//            Join<Employee, EmployeeEducation> eduJoin =
//                    root.join("educations", JoinType.LEFT);
//
//            return cb.equal(eduJoin.get("level"), level);
//        };
//    }

    public static Specification<Employee> educationLevelBetween(
            EducationLevel min,
            EducationLevel max
    ) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }

            List<EducationLevel> levels = educationRange(min, max);

            if (levels.isEmpty()) {
                return cb.disjunction();
            }

            // Subquery Definition
            Subquery<EducationLevel> maxEduSubquery = query.subquery(EducationLevel.class);
            Root<EmployeeEducation> eduRoot = maxEduSubquery.from(EmployeeEducation.class);

            // --- BAGIAN PERBAIKAN ---
            // 1. Ambil path sebagai Object dulu (raw)
            // 2. Cast paksa ke Expression<EducationLevel> agar cb.greatest() mau menerimanya
            // 3. JANGAN gunakan .as(EducationLevel.class)

            @SuppressWarnings("unchecked")
            Expression<EducationLevel> levelExpression = (Expression<EducationLevel>) (Expression<?>) eduRoot.get("level");

            // Select MAX level
            maxEduSubquery.select(cb.greatest(levelExpression));

            // Hubungkan Subquery dengan Parent Query
            maxEduSubquery.where(cb.equal(eduRoot.get("employee"), root));

            return maxEduSubquery.in(levels);
        };
    }

    // Helper method (sedikit dioptimasi agar lebih safe)
    public static List<EducationLevel> educationRange(
            EducationLevel min,
            EducationLevel max
    ) {
        if (min == null && max == null) return List.of();

        // Tentukan default value jika salah satu null
        int minOrd = (min != null) ? min.ordinal() : 0; // Mulai dari index terendah
        int maxOrd = (max != null) ? max.ordinal() : Integer.MAX_VALUE;

        return Arrays.stream(EducationLevel.values())
                .filter(l -> l.ordinal() >= minOrd && l.ordinal() <= maxOrd)
                .toList();
    }


}

