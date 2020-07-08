package parser.converters;

public class StringConverter implements ConverterInterface<String> {

    @Override
    public String convert(String content) {
        return content;
    }

    @Override
    public Class<?> getConversionClass() {
        return String.class;
    }
}
