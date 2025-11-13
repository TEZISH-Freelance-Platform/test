package tezish.tezish.repositories.AdminUser;



import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
