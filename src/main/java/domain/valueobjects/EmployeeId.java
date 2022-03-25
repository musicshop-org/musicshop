package domain.valueobjects;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EmployeeId {

    private final UUID employeeId;

    public EmployeeId() {
        this.employeeId = UUID.randomUUID();
    }
}
