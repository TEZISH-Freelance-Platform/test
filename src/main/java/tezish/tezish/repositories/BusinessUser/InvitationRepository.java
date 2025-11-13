package tezish.tezish.repositories.BusinessUser;

import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.Location.Invitation;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByToken(String token);

}
