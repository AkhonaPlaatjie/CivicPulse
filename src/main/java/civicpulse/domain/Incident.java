package civicpulse.domain;

public class Incident {
    public final String id;
    private String title;
    private String description;
    private String location;
    private Priority priority;
    private Status status;

    Incident(String title, String description, String location, Priority priority) {
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
