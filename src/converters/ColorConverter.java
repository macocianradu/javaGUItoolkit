package converters;

import java.awt.*;

public class ColorConverter implements ConverterInterface<Color> {
    @Override
    public Color convert(String content) {
        return Color.decode(content);
    }
}
