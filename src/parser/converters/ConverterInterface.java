package parser.converters;

import java.io.InvalidClassException;

public interface ConverterInterface<T> {
    T convert(String content) throws InvalidClassException;
    Class<?> getConversionClass();
}
