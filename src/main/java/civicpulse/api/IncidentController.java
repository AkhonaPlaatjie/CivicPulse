package civicpulse.api;

import civicpulse.domain.Incident;
import civicpulse.domain.IncidentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //this one makes the method handles the requests
@RequestMapping("/incidents") //To not repeat the incident on every method
public class IncidentController {

    private final IncidentRepository repository;

public IncidentController(IncidentRepository repository){
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

}
