package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime,
                Duration duration, LocalDateTime ednTime) {
        super(id, name, description, status, startTime, duration);
        this.endTime = ednTime;
        this.subtasksId = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status,
                LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.subtasksId = new ArrayList<>();
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtasksId(int sbId) {
        this.subtasksId.add(sbId);
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSubtaskId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }
}