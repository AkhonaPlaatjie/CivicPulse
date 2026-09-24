package civicpulse.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentRepositoryTest {
    private IncidentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIncidentRepository();
    }

    @Test
    void savedIncidentCanBeFoundById() {
        Incident incident = new Incident("Broken streetligh", "Off for 3 nights", "Main Street", Priority.HIGH);
        repository.save(incident);

        Optional<Incident> found = repository.findById(incident.getId());


        assertTrue(found.isPresent());
        assertEquals(incident.getId(), found.get().getId());
    }

    @Test
    void findByIdReturnEmptyWhenIncidentDoesNotExist(){
        Optional<Incident> found = repository.findById("some-id-that-does-not-exist");
    assertTrue(found.isEmpty());
    }
    @Test
    void findAllReturnAllSavedIncident(){
        Incident first = new Incident("Broken streetlight", "Off for 3 nights", "Main Street", Priority.HIGH);
        Incident second = new Incident("Pothole", "Deep pothole", "5th Ave", Priority.MEDIUM);

        repository.save(first);
        repository.save(second);

        List<Incident> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void findAllReturnsEmptyListWhenNothingSaved() {
        assertTrue(repository.findAll().isEmpty());
    }
}
