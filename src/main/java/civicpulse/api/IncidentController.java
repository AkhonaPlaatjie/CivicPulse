package civicpulse.api;

import civicpulse.domain.InMemoryIncidentRepository;
import civicpulse.domain.Incident;
import civicpulse.domain.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //this one makes the method handles the requests
@RequestMapping("/incidents") //To not repeat the incident on every method
public class IncidentController {
    private final InMemoryIncidentRepository repository;

public IncidentController(InMemoryIncidentRepository repository){
    this.repository = repository;
}

    @PostMapping //handle POST to the class's base path
    @ResponseStatus(HttpStatus.CREATED)
    public Incident createIncident(@RequestBody Incident incident){
    return repository.save(incident);
    }

    @GetMapping
    public List<Incident> ListIncidents(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable String id){
        return repository.findById(id)
                .map(ResponseEntity::ok) //found 200 + body
                .orElseGet(() -> ResponseEntity.notFound().build()); //missing 404

}

@PatchMapping("/{id}/status")
public ResponseEntity<Incident> updateStatus(
        @PathVariable String id,
        @RequestBody StatusUpdateRequest request) {

    Optional<Incident> found = repository.findById(id);
    if (found.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    Incident incident = found.get();

    //2. Dispatch on requested status - 400 if not a valid transition target
    try{
        switch (request.getStatus()) {
            case IN_PROGRESS -> incident.markInProgress();
            case RESOLVED    -> incident.markResolved();
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
    } catch (IllegalStateException e) {
        //3. Invalid transition for current state - 409
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    //4. Success - persist and return 200 with udated incident
    return ResponseEntity.ok(repository.save(incident));
}



}
