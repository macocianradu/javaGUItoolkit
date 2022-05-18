package guiTree.Components;

import guiTree.Visual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import guiTree.Helper.Point2;

public class GridPanel extends Visual {
    private Map<Visual, Point2<Integer>>children;
    private Map<Integer, Integer> fixedRows;
    private Map<Integer, Integer> fixedColumns;
    private List<Integer> rowSizes;
    private List<Integer> columnSizes;
    private Map<Point2<Integer>, Integer> rowPadding;
    private Map<Point2<Integer>, Integer> columnPadding;

    public GridPanel(){
        super();
        children = new HashMap<>();
        rowSizes = new ArrayList<>();
        columnSizes = new ArrayList<>();
        rowPadding = new HashMap<>();
        columnPadding = new HashMap<>();
        fixedRows = new HashMap<>();
        fixedColumns = new HashMap<>();
    }

    public void setSize() {
        super.setSize();
        updateSize();
    }

    private void updateSize() {
        if(rowSizes.size() == 0 && columnSizes.size() == 0) {
            return;
        }

        int setHeights = 0;
        int setWidths = 0;
        for(int i: fixedRows.keySet()) {
            rowSizes.set(i, fixedRows.get(i));
            setHeights += fixedRows.get(i);
        }
        for(int i: fixedColumns.keySet()) {
            columnSizes.set(i, fixedColumns.get(i));
            setWidths += fixedColumns.get(i);
        }

        int height = 0;
        int width = 0;
        if(fixedRows.size() != rowSizes.size()) {
            height = (getHeight() - setHeights) / (rowSizes.size() - fixedRows.size());
        }
        if(fixedColumns.size() != columnSizes.size()) {
            width = (getWidth() - setWidths) / (columnSizes.size() - fixedColumns.size());
        }

        for(int i = 0; i < rowSizes.size(); i++) {
            if(!fixedRows.containsKey(i)) {
                rowSizes.set(i, height);
            }
        }

        for(int i = 0; i < columnSizes.size(); i++) {
            if(!fixedColumns.containsKey(i)) {
                columnSizes.set(i, width);
            }
        }

        for(Visual v : children.keySet()) {
            Point2<Integer> cell = children.get(v);
            Point2<Integer> location = getGridLocation(cell);
            v.setLocation(location.x, location.y);
            int cellWidth = columnSizes.get(cell.x);
            int cellHeight = rowSizes.get(cell.y);
            int rowPad = rowPadding.getOrDefault(cell, 0);
            int colPad = columnPadding.getOrDefault(cell, 0);

            for(int i = 0; i < colPad; i++) {
                cellWidth += columnSizes.get(cell.x + i + 1);
            }
            for(int i = 0; i < rowPad; i++) {
                cellHeight += rowSizes.get(cell.y + i + 1);
            }
            v.setSize(cellWidth, cellHeight);
            if(v.getWidth() != cellWidth) {
                v.setWidth(-1.0f);
                v.setWidth(cellWidth);
            }
            if(v.getHeight() != cellHeight) {
                v.setHeight(-1.0f);
                v.setHeight(cellHeight);
            }
        }
        update();
    }

    public void setRowPadding(Integer row, Integer col, Integer padding) {
        rowPadding.put(new Point2<>(col, row), padding);
        for(int i = rowSizes.size(); i <= row + padding; i++) {
            rowSizes.add(0);
        }
        updateSize();
    }

    public void setColumnPadding(Integer row, Integer col, Integer padding) {
        columnPadding.put(new Point2<>(col, row), padding);
        for(int i = columnSizes.size(); i <= col + padding; i++) {
            columnSizes.add(0);
        }
        updateSize();
    }

    public void setRowSize(Integer row, Integer height) {
        fixedRows.put(row, height);
        updateSize();
    }

    public void setColumnSize(Integer column, Integer width) {
        fixedColumns.put(column, width);
        updateSize();
    }

    private Point2<Integer> getGridLocation(Point2<Integer> grid) {
        int locationX = 0;
        int locationY = 0;
        for(int i = 0; i < grid.x; i++) {
            locationX += columnSizes.get(i);
        }
        for(int i = 0; i < grid.y; i++) {
            locationY += rowSizes.get(i);
        }
        return new Point2<>(locationX, locationY);
    }

    public void addVisual(Visual v) {
        int x = 0;
        int y = 0;
        if(v.getAttribute("row") != null) {
            x = Integer.parseInt(v.getAttribute("row"));
        }
        if(v.getAttribute("column") != null) {
            y = Integer.parseInt(v.getAttribute("column"));
        }
        this.addVisual(v, x, y);
    }

    public void addVisual(Visual v, int row, int col) {
        super.addVisual(v);
        children.put(v, new Point2<>(col, row));
        v.setLocation(-1.0f, -1.0f);
        for(int i = rowSizes.size(); i <= row; i++) {
            rowSizes.add(0);
        }
        for(int i = columnSizes.size(); i <= col; i++) {
            columnSizes.add(0);
        }
        updateSize();
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
    }
}
