package converters;

public class BooleanConverter implements ConverterInterface<Boolean> {

    @Override
    public Boolean convert(String content) {
        return Boolean.valueOf(content);
    }
}
