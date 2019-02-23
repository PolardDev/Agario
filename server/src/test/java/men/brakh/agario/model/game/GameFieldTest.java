package men.brakh.agario.model.game;

import men.brakh.agario.model.Point;
import men.brakh.agario.model.communicator.Communicator;
import men.brakh.agario.model.enums.ChangingType;
import men.brakh.agario.model.message.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameFieldTest {
    private class TestCommunicator implements Communicator {
        List<Message> messages = new ArrayList<>();

        @Override
        public void send(Message message) {
            messages.add(message);
        }
    };

    @Test
    public void add() {
        GameField gameField = new GameField();
        TestCommunicator com1 = new TestCommunicator();
        gameField.add("1", com1);

        assertEquals(com1.messages.get(0).getChangingType(), ChangingType.SPAWN);

    }

    @Test
    public void checkForIntersect() {
    }

    @Test
    public void move() {
        GameField gameField = new GameField();
        TestCommunicator com1 = new TestCommunicator();
        TestCommunicator com2 = new TestCommunicator();

        Person person1 = gameField.add("1", com1);
        Person person2 = gameField.add("2", com2);

        gameField.move(com1, new Point(0,0 ));

        assertEquals(com1.messages.get(com1.messages.size() - 1).getChangingType(), ChangingType.COORDS_CHANGING);
        assertEquals(com2.messages.get(com2.messages.size() - 1).getChangingType(), ChangingType.COORDS_CHANGING);

        assertEquals(com1.messages.get(com1.messages.size() - 1).getValue().getUsername(), "1");
        assertEquals(com2.messages.get(com2.messages.size() - 1).getValue().getUsername(), "1");

        Person mob = gameField.spawnMob();
        assertEquals(com1.messages.get(com1.messages.size() - 1).getChangingType(), ChangingType.SPAWN);
        Point mobCenter = com1.messages.get(com1.messages.size() - 1).getValue().getCenter();

        Communicator mobCommunicator = gameField.getCommunicator(mob);
        gameField.move(mobCommunicator, person2.getCenter());

        assertEquals(com1.messages.get(com1.messages.size() - 1).getChangingType(), ChangingType.DEAD);
        assertEquals(com1.messages.get(com1.messages.size() - 1).getValue().getId(), 3);
    }
}