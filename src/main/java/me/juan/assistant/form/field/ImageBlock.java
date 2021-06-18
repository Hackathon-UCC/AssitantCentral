package me.juan.assistant.form.field;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FormField;

@Accessors(chain = true)
@Setter
public class ImageBlock extends FormField {

    private String url, altText, backgroundColor, height, horizontalAlignment, selectAction, size, style, width, spacing;
    private Boolean separator, isVisible;

    public ImageBlock() {
        super("Image");
    }

    public ImageBlock centerImage() {
        return setSpacing("large").setWidth("85px").setHorizontalAlignment("center");
    }

}
