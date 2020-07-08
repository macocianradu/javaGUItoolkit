package parser.converters;

import java.io.InvalidClassException;

public class FloatConverter implements ConverterInterface<Float> {

    @Override
    public Float convert(String content) throws InvalidClassException {
        content = content.replaceAll(" ", "");

        if(content.toLowerCase().charAt(content.length() - 1) != 'f') {
            throw new InvalidClassException(Float.class.getName());
        }
        content = content.substring(0, content.length() - 1);

        return Float.parseFloat(content);
    }

    @Override
    public Class<?> getConversionClass() {
        return Float.class;
    }
}
