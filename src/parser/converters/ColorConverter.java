package parser.converters;

import java.awt.*;

public class ColorConverter implements ConverterInterface<Color> {
    @Override
    public Color convert(String content) {
        content = content.replaceAll(" ", "");
        return Color.decode(content);
    }

    @Override
    public Class<?> getConversionClass() {
        return Color.class;
    }
}
