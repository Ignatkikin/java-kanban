package tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicEqualsEpicById() {
      Epic epic1 = new Epic("Эпик 1", "Починить авто");
      Epic epic2 = new Epic("Эпик 2", "Съездить на море");
      epic1.setId(1);
      epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}