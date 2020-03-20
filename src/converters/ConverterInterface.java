package converters;

import com.sun.jdi.InvalidTypeException;

public interface ConverterInterface<T> {
    T convert(String content) throws InvalidTypeException;
}
