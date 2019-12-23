package converters;

public class DoubleConverter implements ConverterInterface<Double>{

    @Override
    public Double convert(String content) {
        return Double.parseDouble(content);
    }
}
