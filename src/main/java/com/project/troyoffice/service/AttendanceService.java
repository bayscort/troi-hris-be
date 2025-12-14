package com.project.troyoffice.service;

import com.project.troyoffice.dto.AttendanceDetailResponse;
import com.project.troyoffice.dto.AttendanceRequest;
import com.project.troyoffice.dto.AttendanceResponse;
import com.project.troyoffice.dto.AttendanceStatsResponse;
import com.project.troyoffice.enums.VerificationStatus;
import com.project.troyoffice.mapper.AttendanceMapper;
import com.project.troyoffice.model.Attendance;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.model.User;
import com.project.troyoffice.repository.AttendanceRepository;
import com.project.troyoffice.repository.EmployeeRepository;
import com.project.troyoffice.repository.PlacementRepository;
import com.project.troyoffice.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AttendanceService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final double MAX_DISTANCE_METER = 50.0;
    private static final double EARTH_RADIUS_KM = 6371;

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    private final AttendanceMapper attendanceMapper;

    private final UserContext userContext;

    private final SupabaseStorageService supabaseStorageService;

    private final PlacementRepository placementRepository;

    public AttendanceResponse processAttendance(AttendanceRequest request) {
        User user = userContext.getCurrentUser();

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Optional<Attendance> todayAttendance = attendanceRepository
                .findByEmployeeAndCheckInTimeAfter(employee, startOfDay);

        // A. Tentukan Status Verifikasi Lokasi
        boolean isLocationValid = isWithinRadius(employee,
                request.getLatitude(), request.getLongitude());

        VerificationStatus status = isLocationValid ? VerificationStatus.VERIFIED : VerificationStatus.LOCATION_INVALID;

        if (todayAttendance.isEmpty()) {
            return checkIn(employee, request, status);
        } else {
            return checkOut(todayAttendance.get(), request, status);
        }
    }

    private AttendanceResponse checkIn(Employee employee, AttendanceRequest request, VerificationStatus status) {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setCheckInLatitude(request.getLatitude());
        attendance.setCheckInLongitude(request.getLongitude());

        // Generate filename
        String fileName = "checkin_" + employee.getId() + "_" + System.currentTimeMillis() + ".jpg";

        try {
            String fileUrl = supabaseStorageService.uploadFile(request.getFile(), fileName);
            attendance.setCheckInPhotoUrl(fileUrl);
        } catch (Exception e) {
            throw new RuntimeException("Upload foto gagal: " + e.getMessage());
        }

        attendance.setStatus(status);
        attendance.setLocation(reverseGeocode(request.getLatitude(), request.getLongitude()));

        Attendance saved = attendanceRepository.save(attendance);
        return attendanceMapper.toDTO(saved);
    }


    private AttendanceResponse checkOut(Attendance attendance, AttendanceRequest request, VerificationStatus status) {
        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("Anda sudah melakukan Check-Out hari ini.");
        }

        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setCheckOutLatitude(request.getLatitude());
        attendance.setCheckOutLongitude(request.getLongitude());

        String fileName = "checkout_" + attendance.getEmployee().getId() + "_" + System.currentTimeMillis() + ".jpg";

        try {
            String fileUrl = supabaseStorageService.uploadFile(request.getFile(), fileName);
            attendance.setCheckOutPhotoUrl(fileUrl);
        } catch (Exception e) {
            throw new RuntimeException("Upload foto gagal: " + e.getMessage());
        }

        Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
        attendance.setTotalHours(duration.toMinutes() / 60.0);

        if (status == VerificationStatus.LOCATION_INVALID) {
            attendance.setStatus(VerificationStatus.COUT_LOCATION_INVALID);
        }

        Attendance saved = attendanceRepository.save(attendance);
        return attendanceMapper.toDTO(saved);
    }


    public AttendanceResponse getTodayDetail() {
        User user = userContext.getCurrentUser();

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Attendance attendance = attendanceRepository
                .findByEmployeeAndCheckInTimeAfter(employee, startOfDay).orElse(new Attendance());

        return attendanceMapper.toDTO(attendance);
    }

    public List<AttendanceResponse> getAttendanceList( LocalDate startDate, LocalDate endDate) {
        User user = userContext.getCurrentUser();

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Attendance> list = attendanceRepository
                .findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(employee, start, end);

        return list.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDetailResponse> getAttendanceDetailList(String employeeNumber, LocalDate startDate, LocalDate endDate) {

        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Attendance> list = attendanceRepository
                .findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(employee, start, end);

        return list.stream()
                .map(attendanceMapper::toDetailDTO)
                .collect(Collectors.toList());
    }

    public AttendanceStatsResponse getMonthlyStats(int year, int month) {

        User user = userContext.getCurrentUser();

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Attendance> monthlyAttendance = attendanceRepository
                .findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(employee, start, end);

        return calculateStats(employee, startDate, endDate, monthlyAttendance);
    }

    private AttendanceStatsResponse calculateStats(
            Employee employee, LocalDate startDate, LocalDate endDate, List<Attendance> attendanceList)
    {
        LocalTime ON_TIME_CUTOFF = LocalTime.of(8, 0);

        int totalWorkingDays = 0;
        int daysPresent = attendanceList.size();
        int daysLateCheckIn = 0;
//        long totalMinutesWorked = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                totalWorkingDays++;
            }
        }

        for (Attendance attendance : attendanceList) {
            if (attendance.getCheckInTime().toLocalTime().isAfter(ON_TIME_CUTOFF)) {
                daysLateCheckIn++;
            }
        }

        AttendanceStatsResponse response = new AttendanceStatsResponse();
        response.setEmployeeName(employee.getFullName());
        response.setMonthYear(startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + startDate.getYear());
        response.setTotalWorkingDays(totalWorkingDays);
        response.setDaysPresent(daysPresent);
        response.setDaysAbsent(totalWorkingDays - daysPresent);
        response.setDaysLateCheckIn(daysLateCheckIn);

        return response;
    }

    private boolean isWithinRadius(Employee employee, double checkLat, double checkLon) {

        Optional<Placement> placementOpt =  placementRepository.findPlacementByEmployeeAndIsActiveTrue(employee);

        if (placementOpt.isEmpty()) {
            return false;
        }

        double officeLat = placementOpt.get().getClientSite().getLatitude();
        double officeLon = placementOpt.get().getClientSite().getLongitude();

        // 2. Hitung Jarak Menggunakan Rumus Haversine

        // Konversi derajat ke radian
        double dLat = Math.toRadians(checkLat - officeLat);
        double dLon = Math.toRadians(checkLon - officeLon);

        // Konversi Latitude kantor dan check-in ke radian
        double lat1Rad = Math.toRadians(officeLat);
        double lat2Rad = Math.toRadians(checkLat);

        // Bagian pertama Rumus Haversine: a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        // Bagian kedua Rumus Haversine: c = 2 * atan2(√a, √(1−a))
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Jarak dalam Kilometer: d = R * c
        double distanceKm = EARTH_RADIUS_KM * c;

        // Konversi Jarak ke Meter
        double distanceMeter = distanceKm * 1000;

        // 3. Verifikasi apakah jarak lebih kecil atau sama dengan toleransi
        return distanceMeter <= MAX_DISTANCE_METER;
    }

    public String reverseGeocode(double lat, double lon) {

        String url = String.format(
                "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&addressdetails=1",
                lat, lon
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MyApp");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map body = response.getBody();

        if (body == null || !body.containsKey("address")) {
            return "Unknown location";
        }

        Map address = (Map) body.get("address");

        String suburb = (String) address.getOrDefault("suburb", "");
        String district = (String) address.getOrDefault("city_district", "");
        String city = (String) address.getOrDefault("city", "");
        String country = (String) address.getOrDefault("country", "");

        return String.format("%s, %s, %s, %s",
                suburb, district, city, country
        ).replaceAll("(^, |, ,| ,$)", "").trim();
    }

}
