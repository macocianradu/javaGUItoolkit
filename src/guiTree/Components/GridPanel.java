package guiTree.Components;

import guiTree.Visual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import guiTree.Helper.Point2;

public class GridPanel extends Visual {
    private int columnCount;
    private int rowsCount;
    private Map<Integer, List<Visual>> childrenCols;
    private Map<Integer, List<Visual>> childrenRows;
    private Map<Integer, Integer> rowSizes;
    private Map<Integer, Integer> columnSizes;
    private Map<Point2<Integer>, Integer> rowPadding;
    private Map<Point2<Integer>, Integer> columnPadding;

    public GridPanel(){
        super();
        childrenCols = new TreeMap<>();
        childrenRows = new TreeMap<>();
        rowsCount = 0;
        columnCount = 0;
        rowSizes = new HashMap<>();
        columnSizes = new HashMap<>();
        rowPadding = new HashMap<>();
        columnPadding = new HashMap<>();
    }

    private void refresh() {
        rowsCount = 0;
        columnCount = 0;
        for(int i: childrenRows.keySet()) {
            if(childrenRows.get(i).size() != 0 && !rowSizes.containsKey(i)) {
                rowsCount++;
            }
        }

        for(int i: childrenCols.keySet()) {
            if(childrenCols.get(i).size() != 0 && !columnSizes.containsKey(i)) {
                columnCount++;
            }
        }

        updateSize();
    }

    public void setSize() {
        super.setSize();
        updateSize();
    }

    public void updateSize() {
        if(rowsCount == 0 && columnCount == 0) {
            return;
        }

        int setHeights = 0;
        int setWidths = 0;
        for(int i: rowSizes.keySet()) {
            setHeights += rowSizes.get(i);
        }
        for(int i: columnSizes.keySet()) {
            setWidths += columnSizes.get(i);
        }

        int height = (getHeight() - setHeights) / rowsCount;
        int width = (getWidth() - setWidths) / columnCount;

        int locationY = 0;
        for(int i: childrenRows.keySet()) {
            int actualHeight = rowSizes.getOrDefault(i, height);
            for(Visual v: childrenRows.get(i)) {

                v.setHeight(actualHeight);
                v.setLocationY(locationY);
            }
            locationY += actualHeight;
        }

        int locationX = 0;
        for(int i: childrenCols.keySet()) {
            int actualWidth = columnSizes.getOrDefault(i, width);
            for(Visual v: childrenCols.get(i)) {
                v.setWidth(actualWidth);
                v.setLocationX(locationX);
            }
            locationX += actualWidth;
        }
    }

    public void setRowPadding(int row, int col, int padding) {
        rowPadding.put(new Point2<>(row, col), padding);
    }

    public void setColumnPadding(int row, int col, int padding) {
        columnPadding.put(new Point2<>(row, col), padding);
    }

    public void setRowSize(int row, int height) {
        rowSizes.put(row, height);
        refresh();
    }

    public void setColumnSize(int column, int width) {
        columnSizes.put(column, width);
        refresh();
    }

    public void addVisual(Visual v, int row, int col) {
        super.addVisual(v);
        childrenCols.computeIfAbsent(col, k -> new ArrayList<>());
        childrenRows.computeIfAbsent(row, k -> new ArrayList<>());
        childrenCols.get(col).add(v);
        childrenRows.get(row).add(v);
        v.setLocation(-1.0f, -1.0f);
        refresh();
    }

    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();
        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
    }
}
