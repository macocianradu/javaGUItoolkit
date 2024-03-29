package parser.converters;

public class DoubleConverter implements ConverterInterface<Double>{

    @Override
    public Double convert(String content) {
        content = content.replaceAll(" ", "");
        return Double.parseDouble(content);
    }

    @Override
    public Class<?> getConversionClass() {
        return Double.class;
    }
}
