package converters;

import com.sun.jdi.InvalidTypeException;
import guiTree.Components.Slider;

public class DirectionConverter implements ConverterInterface<Slider.Direction> {
    @Override
    public Slider.Direction convert(String content) throws InvalidTypeException {
        content = content.toLowerCase();
        if(content.equals("horizontal")) {
            return Slider.Direction.Horizontal;
        }
        if(content.equals("vertical")) {
            return Slider.Direction.Vertical;
        }
        throw new InvalidTypeException();
    }
}
