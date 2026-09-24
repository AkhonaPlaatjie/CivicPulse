package civicpulse.domain;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryIncidentRepository implements IncidentRepository  {

    @Override
    public void deleteAll() {
        incidents.clear();
    }

    private final Map<String, Incident> incidents = new HashMap<>();

    @Override
    public Incident save(Incident incident){
        incidents.put(incident.getId(), incident);
        return incident;
    }

    @Override
    public List<Incident> findAll(){

        return new ArrayList<>(incidents.values());
    }

    @Override
    public Optional<Incident> findById(String id){

        return Optional.ofNullable(incidents.get(id));
    }

}
