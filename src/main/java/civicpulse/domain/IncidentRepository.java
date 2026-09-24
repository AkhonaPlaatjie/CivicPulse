package civicpulse.domain;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository {

Incident save(Incident incident);

Optional<Incident> findById(String id);

List<Incident> findAll();

void deleteAll();

}
