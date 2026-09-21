package civicpulse.apiTest;

import civicpulse.api.IncidentController;
import civicpulse.domain.Incident;
import civicpulse.domain.IncidentRepository;
import civicpulse.domain.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
@Import(IncidentRepository.class)
public class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IncidentRepository repository;

    @BeforeEach
    void clearRepository() {
        repository.deleteAll();
    }

    @Test
    void createIncident_returnCreatedIncidentWithId() throws Exception {
        String requestJson = """
                {
                    "title": "Broken streetlight",
                    "description": "Off for 3 nights",
                    "location": "Main Street",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/incidents")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Broken streetlight"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void listIncidentsReturnsEmptyArrayWhenNoneExist() throws Exception {
        mockMvc.perform(get("/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void listIncidentsReturnsAllSavedIncidents() throws Exception {
        repository.save(new Incident("Broken streetlight", "Off for 3 nights", "Main Street", Priority.HIGH));
        repository.save(new Incident("Pothole", "Deep pothole", "5th Ave", Priority.MEDIUM));

        mockMvc.perform(get("/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getIncidentByIdReturnsIncidentWhenFound() throws Exception {
        Incident saved = repository.save(
                new Incident("Broken streetlight", "Off for 3 nights", "Main Street", Priority.HIGH)
        );

        mockMvc.perform(get("/incidents/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.title").value("Broken streetlight"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void getIncidentByIdReturns404WhenNotFound() throws Exception {
        mockMvc.perform(get("/incidents/{id}", "some-id-that-does-not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchIncidentStatusUpdatesOpenToInProgress() throws Exception {
        Incident saved = repository.save(
                new Incident("Broken street", "Off for 3 nights", "Main Street", Priority.HIGH)
        );

        mockMvc.perform(patch("/incidents/{id}/status", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "status": "IN_PROGRESS" }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
}