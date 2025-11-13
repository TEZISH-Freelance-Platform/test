
package tezish.tezish.repositories.BusinessUser;

import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.Location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}