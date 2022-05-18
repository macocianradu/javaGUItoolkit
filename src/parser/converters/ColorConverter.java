package parser.converters;

import java.awt.*;

public class ColorConverter implements ConverterInterface<Color> {
    @Override
    public Color convert(String content) {
        content = content.replaceAll(" ", "");
        if(content.length() == 9) {
            Color newColor = Color.decode(content.substring(0, 7));
            Integer alpha = Integer.decode("#" + content.substring(7, 9));
            return new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), alpha);

        }
        return Color.decode(content);
    }

    @Override
    public Class<?> getConversionClass() {
        return Color.class;
    }
}
