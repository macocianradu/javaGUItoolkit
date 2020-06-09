package guiTree;

import guiTree.events.MouseAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class VisualTest {

    private Visual visual;

    @BeforeEach
    void setUp() {
        visual = new Visual();
    }

    @Test
    void addVisual() {
        Visual child = new Visual();
        child.setName("child");
        visual.addVisual(child);
        Visual exportedChild = visual.findByName(child.getName());
        visual.addVisual(child);
        assertEquals(child, exportedChild);
    }

    @Test
    void removeVisual() {
        Visual child = new Visual();
        child.setName("child");
        visual.addVisual(child);
        Visual inserted = visual.findByName(child.getName());
        assertNotNull(inserted);
        visual.removeVisual(child);
        inserted = visual.findByName(child.getName());
        assertNull(inserted);
    }

    @Test
    void setSize() {
        int width = 100;
        int height = 100;
        visual.setSize(width, height);
        assertEquals(visual.getHeight(), height);
        assertEquals(visual.getWidth(), width);

        Visual child = new Visual();
        visual.addVisual(child);

        child.setSize(0.0f, 0.0f);
        width = 0;
        height = 0;
        assertEquals(child.getHeight(), height);
        assertEquals(child.getWidth(), width);


        child.setSize(0.5f, 0.5f);
        width = 50;
        height = 50;
        assertEquals(child.getHeight(), height);
        assertEquals(child.getWidth(), width);


        child.setSize(0.75f, 0.75f);
        width = 75;
        height = 75;
        assertEquals(child.getHeight(), height);
        assertEquals(child.getWidth(), width);

        child.setSize(1.0f, 1.0f);
        width = 100;
        height = 100;
        assertEquals(child.getHeight(), height);
        assertEquals(child.getWidth(), width);

        child.setSize(-1f, -1f);
        child.setSize(1, 1);
        width = 1;
        height = 1;
        assertEquals(child.getHeight(), height);
        assertEquals(child.getWidth(), width);
    }

    @Test
    void setLocation() {
        visual.setSize(100, 100);
        Visual child = new Visual();
        visual.addVisual(child);

        child.setLocation(50, 75);
        assertEquals(50, child.getLocationX());
        assertEquals(75, child.getLocationY());

        child.setLocation(0.0f, 0.0f);
        assertEquals(0, child.getLocationX());
        assertEquals(0, child.getLocationY());

        child.setLocation(0.5f, 0.75f);
        assertEquals(50, child.getLocationX());
        assertEquals(75, child.getLocationY());

        child.setLocation("top_left");
        child.setMargins(50);
        assertEquals(50, child.getLocationX());
        assertEquals(50, child.getLocationY());

        child.setLocation("top_center");
        child.setMargins(0);
        assertEquals(49, child.getLocationX());
        assertEquals(0, child.getLocationY());

        child.setLocation("top_right");
        assertEquals(99, child.getLocationX());
        assertEquals(0, child.getLocationY());

        child.setLocation("middle_left");
        assertEquals(0, child.getLocationX());
        assertEquals(49, child.getLocationY());

        child.setLocation("middle_center");
        assertEquals(49, child.getLocationX());
        assertEquals(49, child.getLocationY());

        child.setLocation("middle_right");
        assertEquals(99, child.getLocationX());
        assertEquals(49, child.getLocationY());

        child.setLocation("bottom_left");
        assertEquals(0, child.getLocationX());
        assertEquals(99, child.getLocationY());

        child.setLocation("bottom_center");
        assertEquals(49, child.getLocationX());
        assertEquals(99, child.getLocationY());

        child.setLocation("bottom_right");
        assertEquals(99, child.getLocationX());
        assertEquals(99, child.getLocationY());
    }

    @Test
    void mouseEvents() {
        MouseEvent mouseEvent = new MouseEvent(new JPanel(), 0, 0, 0, 20, 20, 0, false);
        Visual child = new Visual(50, 50);
        Visual child2 = new Visual(100, 25);

        visual.addVisual(child);
        visual.addVisual(child2);

        final Boolean[] activatedChild = {false};
        final Boolean[] activatedChild2 = {false};
        final Boolean[] activatedVisual = {false};

        child.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                activatedChild[0] = true;
            }
        });

        child2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                activatedChild2[0] = true;
            }
        });

        visual.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                activatedVisual[0] = true;
            }
        });

        visual.mouseMoved(mouseEvent);
        assertEquals(false, activatedChild[0]);
        assertEquals(true, activatedChild2[0]);
        assertEquals(false, activatedVisual[0]);
        activatedChild[0] = false;
        activatedChild2[0] = false;
        activatedVisual[0] = false;

        mouseEvent = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), 75, 20, 0, false);
        visual.mouseMoved(mouseEvent);
        assertEquals(false, activatedChild[0]);
        assertEquals(true, activatedChild2[0]);
        assertEquals(false, activatedVisual[0]);
        activatedChild[0] = false;
        activatedChild2[0] = false;
        activatedVisual[0] = false;

        mouseEvent = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), 25, 40, 0, false);
        visual.mouseMoved(mouseEvent);
        assertEquals(true, activatedChild[0]);
        assertEquals(false, activatedChild2[0]);
        assertEquals(false, activatedVisual[0]);
        activatedChild[0] = false;
        activatedChild2[0] = false;
        activatedVisual[0] = false;

        mouseEvent = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), 75, 75, 0, false);
        visual.mouseMoved(mouseEvent);
        assertEquals(false, activatedChild[0]);
        assertEquals(false, activatedChild2[0]);
        assertEquals(true, activatedVisual[0]);
    }
}
