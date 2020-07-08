package parser.converters;

import guiTree.Components.Slider;

import java.io.InvalidClassException;

public class DirectionConverter implements ConverterInterface<Slider.Direction> {
    @Override
    public Slider.Direction convert(String content) throws InvalidClassException {
        content = content.toLowerCase();
        if(content.equals("horizontal")) {
            return Slider.Direction.Horizontal;
        }
        if(content.equals("vertical")) {
            return Slider.Direction.Vertical;
        }
        throw new InvalidClassException(Slider.Direction.class.getName());
    }

    @Override
    public Class<?> getConversionClass() {
        return Slider.Direction.class;
    }
}
