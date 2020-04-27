package parser.converters;

import com.sun.jdi.InvalidTypeException;

public class FloatConverter implements ConverterInterface<Float> {

    @Override
    public Float convert(String content) throws InvalidTypeException {
        content = content.replaceAll(" ", "");

        if(content.toLowerCase().charAt(content.length() - 1) != 'f') {
            throw new InvalidTypeException();
        }
        content = content.substring(0, content.length() - 1);

        return Float.parseFloat(content);
    }
}
