package civicpulse.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Repository
@Profile("dynamodb")
public class DynamoDbIncidentRepository implements IncidentRepository {

    private static final String TABLE_NAME = "Incidents";
    private final DynamoDbClient client = DynamoDbClient.create(); // reads ~/.aws credentials automatically

    @Override
    public Incident save(Incident incident) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(incident.getId()).build());
        item.put("title", AttributeValue.builder().s(incident.getTitle()).build());
        item.put("description", AttributeValue.builder().s(incident.getDescription()).build());
        item.put("location", AttributeValue.builder().s(incident.getLocation()).build());
        item.put("priority", AttributeValue.builder().s(incident.getPriority().toString()).build());
        item.put("status", AttributeValue.builder().s(incident.getStatus().toString()).build());

        client.putItem(PutItemRequest.builder().tableName(TABLE_NAME).item(item).build());
        return incident;
    }

    @Override
    public Optional<Incident> findById(String id) {
        Map<String, AttributeValue> key = Map.of("id", AttributeValue.builder().s(id).build());
        GetItemResponse response = client.getItem(GetItemRequest.builder().tableName(TABLE_NAME).key(key).build());

        if (!response.hasItem()) {
            return Optional.empty();
        }
        return Optional.of(mapToIncident(response.item()));
    }

    @Override
    public List<Incident> findAll() {
        ScanResponse response = client.scan(ScanRequest.builder().tableName(TABLE_NAME).build());
        List<Incident> incidents = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            incidents.add(mapToIncident(item));
        }
        return incidents;
    }

    @Override
    public void deleteAll() {
        for (Incident incident : findAll()) {
            Map<String, AttributeValue> key = Map.of("id", AttributeValue.builder().s(incident.getId()).build());
            client.deleteItem(DeleteItemRequest.builder().tableName(TABLE_NAME).key(key).build());
        }
    }

    private Incident mapToIncident(Map<String, AttributeValue> item) {
        return Incident.reconstruct(
                item.get("id").s(),
                item.get("title").s(),
                item.get("description").s(),
                item.get("location").s(),
                Priority.valueOf(item.get("priority").s()),
                Status.valueOf(item.get("status").s())
        );
    }
}