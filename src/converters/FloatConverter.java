package converters;

import com.sun.jdi.InvalidTypeException;

public class FloatConverter implements ConverterInterface<Float> {

    @Override
    public Float convert(String content) throws InvalidTypeException {
        float number = Float.parseFloat(content);
        if(number > 1 || number < 0) {
            throw new InvalidTypeException();
        }
        return Float.parseFloat(content);
    }
}
