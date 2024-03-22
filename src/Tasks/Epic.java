package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtasksId(int sbId) {
        this.subtasksId.add(sbId);
    }
}