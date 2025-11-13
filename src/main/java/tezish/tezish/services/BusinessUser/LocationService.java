package tezish.tezish.services.BusinessUser;

import org.springframework.stereotype.Service;
import tezish.tezish.models.Location.Location;
import tezish.tezish.repositories.BusinessUser.LocationRepository;

import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }
}