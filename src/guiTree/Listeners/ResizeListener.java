package guiTree.Listeners;

import guiTree.Helper.Point2d;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ResizeListener extends MouseAdapter {
    public Point2d location;
    public Point2d size;
    public Cursor cursor;
    private Boolean moving = false;
    private Boolean resizing = false;
    private Point2d initialLocation;
    private int resizeDelta = 5;
    private Boolean NORTH;
    private Boolean SOUTH;
    private Boolean WEST;
    private Boolean EAST;

    public ResizeListener(Direction[] directions, Point2d size, Point2d location) {
        this.cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        this.size = size;
        this.location = location;
        NORTH = false;
        SOUTH = false;
        WEST = false;
        EAST = false;
        for(Direction dir:directions) {
            if(dir == Direction.NORTH) {
                NORTH = true;
            }
            if(dir == Direction.SOUTH) {
                SOUTH = true;
            }
            if(dir == Direction.WEST) {
                WEST = true;
            }
            if(dir == Direction.EAST) {
                EAST = true;
            }
            if(dir == Direction.ALL) {
                NORTH = true;
                SOUTH = true;
                WEST = true;
                EAST = true;
            }
        }
    }

    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
    }

    public void setSize(int x, int y) {
        size.x = x;
        size.y = y;
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        this.cursor = new Cursor(Cursor.DEFAULT_CURSOR);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(moving) {
            setLocation(mouseEvent.getXOnScreen() - initialLocation.x, mouseEvent.getYOnScreen() - initialLocation.y);
        }
        else if(!resizing){
            this.resizing = true;
            this.resize(mouseEvent);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(cursor.getType() != Cursor.DEFAULT_CURSOR){
            this.moving = false;
        }
        else {
            this.moving = true;
            this.resizing = false;
        }
        initialLocation = new Point2d(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        moving = false;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if(mouseEvent.getX() < resizeDelta || mouseEvent.getX() > size.x - resizeDelta ||
                mouseEvent.getY() < resizeDelta || mouseEvent.getY() > size.y - resizeDelta) {
            this.setResizeCursor(mouseEvent);
        }
        else {
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        }
    }

    private void setResizeCursor(MouseEvent e){
        if(e.getX() <= resizeDelta) {
            if(e.getY() <= resizeDelta && (NORTH && WEST)) {
                cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
            }
            else if(e.getY() >= size.y - resizeDelta && (SOUTH && WEST)){
                cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
            }
            else if(WEST){
                cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
            }
        }
        else if(e.getX() >= size.x - resizeDelta){
            if(e.getY() <= resizeDelta && (NORTH && EAST)){
                cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
            }
            else if(e.getY() >= size.y - resizeDelta && (SOUTH && EAST)){
                cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
            }
            else if(EAST){
                cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
            }
        }
        else{
            if(e.getY() <= resizeDelta && (NORTH)){
                cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
            }
            else if(e.getY() >= size.y - resizeDelta && (SOUTH)){
                cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
            }
        }
    }

    private void resize(MouseEvent e) {
        switch (cursor.getType()) {
            case Cursor.N_RESIZE_CURSOR:
                setSize(size.x, size.y + initialLocation.y - e.getYOnScreen());
                setLocation(location.x, e.getYOnScreen());
                initialLocation.y = e.getYOnScreen();
                break;
            case Cursor.NE_RESIZE_CURSOR:
                setSize(size.x + e.getXOnScreen() - initialLocation.x, size.y + initialLocation.y - e.getYOnScreen());
                setLocation(location.x, e.getYOnScreen());
                initialLocation.x = e.getXOnScreen();
                initialLocation.y = e.getYOnScreen();
                break;
            case Cursor.NW_RESIZE_CURSOR:
                setSize(size.x + initialLocation.x - e.getXOnScreen(), size.y + initialLocation.y - e.getYOnScreen());
                setLocation(e.getXOnScreen(), e.getYOnScreen());
                initialLocation.x = e.getXOnScreen();
                initialLocation.y = e.getYOnScreen();
                break;
            case Cursor.E_RESIZE_CURSOR:
                setSize(size.x + e.getXOnScreen() - initialLocation.x, size.y);
                initialLocation.x = e.getXOnScreen();
                break;
            case Cursor.W_RESIZE_CURSOR:
                setSize(size.x + initialLocation.x - e.getXOnScreen(), size.y);
                setLocation(e.getXOnScreen(), location.y);
                initialLocation.x = e.getXOnScreen();
                break;
            case Cursor.SE_RESIZE_CURSOR:
                setSize(size.x + e.getXOnScreen() - initialLocation.x, size.y + e.getYOnScreen() - initialLocation.y);
                initialLocation.x = e.getXOnScreen();
                initialLocation.y = e.getYOnScreen();
                break;
            case Cursor.S_RESIZE_CURSOR:
                setSize(size.x, size.y + e.getYOnScreen() - initialLocation.y);
                initialLocation.y = e.getYOnScreen();
                break;
            case Cursor.SW_RESIZE_CURSOR:
                setSize(size.x + initialLocation.x - e.getXOnScreen(), size.y + e.getYOnScreen() - initialLocation.y);
                setLocation(e.getXOnScreen(), location.y);
                initialLocation.x = e.getXOnScreen();
                initialLocation.y = e.getYOnScreen();
                break;
        }
        this.resizing = false;
    }
}
