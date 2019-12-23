package converters;

public class StringConverter implements ConverterInterface<String> {

    @Override
    public String convert(String content) {
        return content;
    }
}
