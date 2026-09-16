package civicpulse.domain;

import civicpulse.domain.Incident;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class IncidentTest {

    private Incident incident;

    @BeforeEach
    void setUp() {
        incident = new Incident("Broken streetlight", "Off for 3 nights", "Main Street", Priority.HIGH);
    }

    @Test
    void incidentStoresFieldsCorrectly() {
        assertNotNull(incident.getId());
        assertEquals("Broken streetlight", incident.getTitle());
        assertEquals("Off for 3 nights", incident.getDescription());
        assertEquals("Main Street", incident.getLocation());
        assertEquals(Priority.HIGH, incident.getPriority());
    }


    @Test
    void newIncidentStartsWithStatusOpen() {

        assertEquals(Status.OPEN, incident.getStatus());
    }

    @Test
    void cannotCreateIncidentWithBlankTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                new Incident("", "Off for 3 nights", "Main Street", Priority.HIGH));
    }

    @Test
    void eachIncidentHasAUniqueId() {
        Incident other = new Incident("Pothole", "Deep pothole", "5th Ave", Priority.MEDIUM);
        assertNotEquals(incident.getId(), other.getId());
    }
    @Test
    void statusCanTransitionFromOpenToInProgress(){
        incident.markInProgress();
        assertEquals(Status.IN_PROGRESS, incident.getStatus());
    }
    @Test
    void statusCannotSkipDirectlyFromOpenToResolved() {
        assertThrows(IllegalStateException.class, () -> incident.markResolved());
    }

    @Test
    void inProgressIncidentCanBeResolved() {
        incident.markInProgress();
        incident.markResolved();
        assertEquals(Status.RESOLVED, incident.getStatus());
    }
}