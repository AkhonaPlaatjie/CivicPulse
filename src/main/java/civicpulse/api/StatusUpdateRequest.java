package civicpulse.api;

import civicpulse.domain.Status;

public class StatusUpdateRequest {
    private Status status;

    public Status getStatus() { return status; }
    public void setStatus(Status status){this.status = status;}

}
