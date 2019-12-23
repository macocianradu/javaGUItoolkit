package converters;

public class FloatConverter implements ConverterInterface<Float> {

    @Override
    public Float convert(String content) {
        return Float.parseFloat(content);
    }
}
