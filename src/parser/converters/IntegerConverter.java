package parser.converters;

public class IntegerConverter implements ConverterInterface<Integer> {

    @Override
    public Integer convert(String content) {
        content = content.replaceAll(" ", "");
        return Integer.parseInt(content);
    }

    @Override
    public Class<?> getConversionClass() {
        return Integer.class;
    }
}
