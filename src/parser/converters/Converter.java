package parser.converters;

import com.sun.jdi.InvalidTypeException;
import guiTree.Components.Slider;

import java.awt.*;
import java.util.HashMap;

public class Converter {
    private HashMap<Class<?>, ConverterInterface<?>> converterTable;

    public Converter(){
        this.converterTable = new HashMap<>();
        this.converterTable.put(Integer.class, new IntegerConverter());
        this.converterTable.put(Float.class, new FloatConverter());
        this.converterTable.put(Double.class, new DoubleConverter());
        this.converterTable.put(String.class, new StringConverter());
        this.converterTable.put(Boolean.class, new BooleanConverter());
        this.converterTable.put(Integer.TYPE, new IntegerConverter());
        this.converterTable.put(Float.TYPE, new FloatConverter());
        this.converterTable.put(Double.TYPE, new DoubleConverter());
        this.converterTable.put(Boolean.TYPE, new BooleanConverter());
        this.converterTable.put(Color.class, new ColorConverter());
        this.converterTable.put(Slider.Direction.class, new DirectionConverter());
    }

    public Object objectCreatorFactory (Class<?> type, String content) throws InvalidTypeException {
        if(this.converterTable.containsKey(type)) {
            return this.converterTable.get(type).convert(content);
        }
        throw new InvalidTypeException();
    }
}
