package parser.converters;

public class BooleanConverter implements ConverterInterface<Boolean> {

    @Override
    public Boolean convert(String content) {
        content = content.replaceAll(" ", "");
        return Boolean.valueOf(content);
    }
}
