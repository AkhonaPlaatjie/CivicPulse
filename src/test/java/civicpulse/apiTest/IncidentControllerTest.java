package civicpulse.apiTest;

import civicpulse.api.IncidentController;
import civicpulse.domain.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
@Import(IncidentRepository.class)
public class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createIncident_returnCreatedIncidentWithId() throws Exception{
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
}
