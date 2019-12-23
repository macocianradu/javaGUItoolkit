package converters;

public class IntegerConverter implements ConverterInterface<Integer> {

    @Override
    public Integer convert(String content) {
        return Integer.parseInt(content);
    }
}
