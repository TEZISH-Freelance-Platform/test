package tezish.tezish.services.JobShift;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tezish.tezish.models.Response.ShiftJobResponse.PostResponse;
import tezish.tezish.repositories.JobShifts.JobRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
public class PostService {

    private final JobRepository jobRepository;

    public PostService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

@Transactional(readOnly = true)
public List<PostResponse> getFreelancerPosts(boolean isfilled) {
    List<PostResponse> allPosts = jobRepository.findAllPosts().stream()
            .map(row -> {
                PostResponse response = new PostResponse();
                response.setPostId(((Number) row[0]).longValue());
                response.setShiftId(((Number) row[0]).longValue());
                response.setJobName((String) row[2]);
                response.setJobImage((String) row[3]);
                response.setEmoji((String) row[4]);
                response.setCompanyName((String) row[5]);
                LocalDateTime startDate = ((java.sql.Timestamp) row[8]).toLocalDateTime();
                LocalDateTime endDate = ((java.sql.Timestamp) row[9]).toLocalDateTime();
                
                response.setShiftStartTime(String.format("%02d:%02d", startDate.getHour(), startDate.getMinute()));
                response.setShiftEndTime(String.format("%02d:%02d", endDate.getHour(), endDate.getMinute()));
                response.setShiftStartDate(startDate);
                response.setShiftEndDate(endDate);
                
                int freelancerNeeded = ((Number) row[10]).intValue();
                int freelancerAccepted = ((Number) row[12]).intValue();
                response.setFreelancerNeeded(freelancerNeeded);
                response.setPrice(row[11].toString());
                response.setFreelancerAccepted(freelancerAccepted);
                
                boolean isPriority = row[13] != null && (Boolean) row[13];
                LocalDateTime priorityExpiresAt = row[14] != null ? ((java.sql.Timestamp) row[14]).toLocalDateTime() : null;
                String message = row[15] != null ? (String) row[15] : null;
                response.setPriority(isPriority && (priorityExpiresAt == null || priorityExpiresAt.isAfter(LocalDateTime.now())));
                response.setMessage(message);
                
                return response;
            })
            .sorted((p1, p2) -> {
                boolean p1Finished = p1.getShiftStartDate().toLocalDate().isBefore(LocalDate.now());
                boolean p2Finished = p2.getShiftStartDate().toLocalDate().isBefore(LocalDate.now());
                if (p1Finished && !p2Finished) return 1;
                if (!p1Finished && p2Finished) return -1;
                
                if (!p1Finished) {
                    if (p1.isPriority() && !p2.isPriority()) return -1;
                    if (!p1.isPriority() && p2.isPriority()) return 1;
                    return p1.getShiftStartDate().compareTo(p2.getShiftStartDate());
                }
                
                return p2.getShiftStartDate().compareTo(p1.getShiftStartDate());
            })
            .toList();

    LocalDate today = LocalDate.now();
    LocalDate windowEnd = today.plusDays(2); // today, tomorrow, day after tomorrow

    List<PostResponse> visibleWindowShifts = allPosts.stream()
            .filter(post -> {
                LocalDate date = post.getShiftStartDate().toLocalDate();
                return !date.isBefore(today) && !date.isAfter(windowEnd);
            })
            .filter(post -> post.getShiftEndDate() != null && post.getShiftEndDate().isAfter(LocalDateTime.now()))
            .filter(post -> isfilled || post.getFreelancerNeeded() > post.getFreelancerAccepted())
            .toList();

    return visibleWindowShifts;
}

    public Page<PostResponse> getBusinessPostsPage(Pageable pageable) {
        List<PostResponse> allPosts = jobRepository.findAllPosts().stream()
                .map(row -> {
                    PostResponse response = new PostResponse();
                    response.setPostId(((Number) row[0]).longValue());
                    response.setShiftId(((Number) row[0]).longValue());
                    response.setJobName((String) row[2]);
                    response.setJobImage((String) row[3]);
                    response.setEmoji((String) row[4]);
                    response.setCompanyName((String) row[5]);
                    LocalDateTime startDate = ((java.sql.Timestamp) row[8]).toLocalDateTime();
                    LocalDateTime endDate = ((java.sql.Timestamp) row[9]).toLocalDateTime();

                    response.setShiftStartTime(String.format("%02d:%02d", startDate.getHour(), startDate.getMinute()));
                    response.setShiftEndTime(String.format("%02d:%02d", endDate.getHour(), endDate.getMinute()));
                    response.setShiftStartDate(startDate);
                    response.setShiftEndDate(endDate);

                    int freelancerNeeded = ((Number) row[10]).intValue();
                    int freelancerAccepted = ((Number) row[12]).intValue();
                    response.setFreelancerNeeded(freelancerNeeded);
                    response.setPrice(row[11].toString());
                    response.setFreelancerAccepted(freelancerAccepted);

                    boolean isPriority = row[13] != null && (Boolean) row[13];
                    LocalDateTime priorityExpiresAt = row[14] != null ? ((java.sql.Timestamp) row[14]).toLocalDateTime() : null;
                    String message = row[15] != null ? (String) row[15] : null;
                    response.setPriority(isPriority && (priorityExpiresAt == null || priorityExpiresAt.isAfter(LocalDateTime.now())));
                    response.setMessage(message);

                    return response;
                })
                .sorted((p1, p2) -> {
                    // Priority first (true before false)
                    if (p1.isPriority() && !p2.isPriority()) return -1;
                    if (!p1.isPriority() && p2.isPriority()) return 1;
                    // Then by start date ascending (nulls last)
                    if (p1.getShiftStartDate() == null && p2.getShiftStartDate() != null) return 1;
                    if (p1.getShiftStartDate() != null && p2.getShiftStartDate() == null) return -1;
                    if (p1.getShiftStartDate() != null && p2.getShiftStartDate() != null) {
                        int cmp = p1.getShiftStartDate().compareTo(p2.getShiftStartDate());
                        if (cmp != 0) return cmp;
                    }
                    // Finally by shift id to stabilize order
                    return Long.compare(p1.getShiftId(), p2.getShiftId());
                })
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allPosts.size());
        List<PostResponse> pageContent = start >= allPosts.size() ? List.of() : allPosts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allPosts.size());
    }

    public Page<PostResponse> getBusinessPostsPage(java.time.LocalDate startDate,
                                                   java.time.LocalDate endDate,
                                                   org.springframework.data.domain.Pageable pageable) {
        List<PostResponse> allPosts = jobRepository.findAllPosts().stream()
                .map(row -> {
                    PostResponse response = new PostResponse();
                    response.setPostId(((Number) row[0]).longValue());
                    response.setShiftId(((Number) row[0]).longValue());
                    response.setJobName((String) row[2]);
                    response.setJobImage((String) row[3]);
                    response.setEmoji((String) row[4]);
                    response.setCompanyName((String) row[5]);
                    java.time.LocalDateTime startDt = ((java.sql.Timestamp) row[8]).toLocalDateTime();
                    java.time.LocalDateTime endDt = ((java.sql.Timestamp) row[9]).toLocalDateTime();

                    response.setShiftStartTime(String.format("%02d:%02d", startDt.getHour(), startDt.getMinute()));
                    response.setShiftEndTime(String.format("%02d:%02d", endDt.getHour(), endDt.getMinute()));
                    response.setShiftStartDate(startDt);
                    response.setShiftEndDate(endDt);

                    int freelancerNeeded = ((Number) row[10]).intValue();
                    int freelancerAccepted = ((Number) row[12]).intValue();
                    response.setFreelancerNeeded(freelancerNeeded);
                    response.setPrice(row[11].toString());
                    response.setFreelancerAccepted(freelancerAccepted);

                    boolean isPriority = row[13] != null && (Boolean) row[13];
                    java.time.LocalDateTime priorityExpiresAt = row[14] != null ? ((java.sql.Timestamp) row[14]).toLocalDateTime() : null;
                    String message = row[15] != null ? (String) row[15] : null;
                    response.setPriority(isPriority && (priorityExpiresAt == null || priorityExpiresAt.isAfter(java.time.LocalDateTime.now())));
                    response.setMessage(message);

                    return response;
                })
                .sorted((p1, p2) -> {
                    if (p1.isPriority() && !p2.isPriority()) return -1;
                    if (!p1.isPriority() && p2.isPriority()) return 1;
                    if (p1.getShiftStartDate() == null && p2.getShiftStartDate() != null) return 1;
                    if (p1.getShiftStartDate() != null && p2.getShiftStartDate() == null) return -1;
                    if (p1.getShiftStartDate() != null && p2.getShiftStartDate() != null) {
                        int cmp = p1.getShiftStartDate().compareTo(p2.getShiftStartDate());
                        if (cmp != 0) return cmp;
                    }
                    return Long.compare(p1.getShiftId(), p2.getShiftId());
                })
                .toList();

        if (startDate != null && endDate != null) {
            java.time.LocalDate start = startDate;
            java.time.LocalDate end = endDate;
            if (end.isBefore(start)) {
                java.time.LocalDate tmp = start;
                start = end;
                end = tmp;
            }
            java.time.LocalDateTime from = start.atStartOfDay();
            java.time.LocalDateTime to = end.atTime(23, 59, 59);
            allPosts = allPosts.stream()
                    .filter(p -> p.getShiftStartDate() != null)
                    .filter(p -> !p.getShiftStartDate().isBefore(from) && !p.getShiftStartDate().isAfter(to))
                    .toList();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allPosts.size());
        List<PostResponse> pageContent = start >= allPosts.size() ? List.of() : allPosts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allPosts.size());
    }

    public java.util.List<PostResponse> getBusinessPostsForDateRange(java.time.LocalDate startDate,
                                                                     java.time.LocalDate endDate) {
        java.util.List<PostResponse> allPosts = jobRepository.findAllPosts().stream()
                .map(row -> {
                    PostResponse response = new PostResponse();
                    response.setPostId(((Number) row[0]).longValue());
                    response.setShiftId(((Number) row[0]).longValue());
                    response.setJobName((String) row[2]);
                    response.setJobImage((String) row[3]);
                    response.setEmoji((String) row[4]);
                    response.setCompanyName((String) row[5]);
                    java.time.LocalDateTime startDt = ((java.sql.Timestamp) row[8]).toLocalDateTime();
                    java.time.LocalDateTime endDt = ((java.sql.Timestamp) row[9]).toLocalDateTime();

                    response.setShiftStartTime(String.format("%02d:%02d", startDt.getHour(), startDt.getMinute()));
                    response.setShiftEndTime(String.format("%02d:%02d", endDt.getHour(), endDt.getMinute()));
                    response.setShiftStartDate(startDt);
                    response.setShiftEndDate(endDt);

                    int freelancerNeeded = ((Number) row[10]).intValue();
                    int freelancerAccepted = ((Number) row[12]).intValue();
                    response.setFreelancerNeeded(freelancerNeeded);
                    response.setPrice(row[11].toString());
                    response.setFreelancerAccepted(freelancerAccepted);

                    boolean isPriority = row[13] != null && (Boolean) row[13];
                    java.time.LocalDateTime priorityExpiresAt = row[14] != null ? ((java.sql.Timestamp) row[14]).toLocalDateTime() : null;
                    String message = row[15] != null ? (String) row[15] : null;
                    response.setPriority(isPriority && (priorityExpiresAt == null || priorityExpiresAt.isAfter(java.time.LocalDateTime.now())));
                    response.setMessage(message);

                    return response;
                })
                .sorted((p1, p2) -> {
                    if (p1.isPriority() && !p2.isPriority()) return -1;
                    if (!p1.isPriority() && p2.isPriority()) return 1;
                    if (p1.getShiftStartDate() == null && p2.getShiftStartDate() != null) return 1;
                    if (p1.getShiftStartDate() != null && p2.getShiftStartDate() == null) return -1;
                    if (p1.getShiftStartDate() != null && p2.getShiftStartDate() != null) {
                        int cmp = p1.getShiftStartDate().compareTo(p2.getShiftStartDate());
                        if (cmp != 0) return cmp;
                    }
                    return Long.compare(p1.getShiftId(), p2.getShiftId());
                })
                .toList();

        if (startDate != null && endDate != null) {
            java.time.LocalDate start = startDate;
            java.time.LocalDate end = endDate;
            if (end.isBefore(start)) {
                java.time.LocalDate tmp = start;
                start = end;
                end = tmp;
            }
            java.time.LocalDateTime from = start.atStartOfDay();
            java.time.LocalDateTime to = end.atTime(23, 59, 59);
            allPosts = allPosts.stream()
                    .filter(p -> p.getShiftStartDate() != null)
                    .filter(p -> !p.getShiftStartDate().isBefore(from) && !p.getShiftStartDate().isAfter(to))
                    .toList();
        }

        return allPosts;
    }

}

