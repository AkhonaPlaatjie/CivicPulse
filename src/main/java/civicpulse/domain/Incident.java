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
}
