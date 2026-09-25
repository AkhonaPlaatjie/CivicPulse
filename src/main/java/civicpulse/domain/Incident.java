package civicpulse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Incident {
    public final String id;
    private String title;
    private String description;
    private String location;
    public Priority priority;
    private Status status;

   public Incident(
           @JsonProperty("title") String title,
           @JsonProperty("description") String description,
           @JsonProperty("location") String location,
           @JsonProperty("priority") Priority priority) {
       if (title == null || title.isBlank()){
           throw new IllegalArgumentException("Title cannot be null or blank");
       }
        this.id = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.location = location;
        this.priority = priority;
        this.status = Status.OPEN;


    }
    //reconstructor

    private Incident(String id, String title, String description, String location, Priority priority, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.priority = priority;
        this.status = status;
    }

    public static Incident reconstruct(String id, String title, String description, String location, Priority priority, Status status) {
        return new Incident(id, title, description, location, priority, status);
    }

    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Priority getPriority() {
        return priority;

    }
    public Status getStatus(){return status;}

    public void markInProgress() {
        if (status != Status.OPEN) {
            throw new IllegalStateException("Can only move to IN_PROGRESS from OPEN");
        }
        this.status = Status.IN_PROGRESS;
    }

    public void markResolved() {
        if (status != Status.IN_PROGRESS) {
            throw new IllegalStateException("Can only resolve an incident that is IN_PROGRESS");
        }
        this.status = Status.RESOLVED;
    }
}
