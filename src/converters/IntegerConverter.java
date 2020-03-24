package converters;

public class IntegerConverter implements ConverterInterface<Integer> {

    @Override
    public Integer convert(String content) {
        content = content.replaceAll(" ", "");
        return Integer.parseInt(content);
    }
}
