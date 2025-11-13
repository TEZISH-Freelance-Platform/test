package tezish.tezish.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tezish.tezish.models.Job_Shift.Shift;
import tezish.tezish.repositories.JobShifts.ShiftRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShiftPriorityService {


    private final ShiftRepository shiftRepository;

    public ShiftPriorityService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Transactional
    public void setShiftPriority(Long shiftId, int durationMinutes) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        
        shift.setPriority(true);
        shift.setPriorityExpiresAt(LocalDateTime.now().plusMinutes(durationMinutes));
        shift.setMessage("Qəbul ehtimalı 📈");
        shiftRepository.save(shift);
    }


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void clearExpiredPriorities() {
        List<Shift> expiredShifts = shiftRepository.findExpiredPriorityShifts(LocalDateTime.now());
        for (Shift shift : expiredShifts) {
            shift.setPriority(false);
            shift.setPriorityExpiresAt(null);
            shift.setMessage(null);
        }
        shiftRepository.saveAll(expiredShifts);
    }
} 