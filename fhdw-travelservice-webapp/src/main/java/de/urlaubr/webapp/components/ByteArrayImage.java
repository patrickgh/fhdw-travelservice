package de.urlaubr.webapp.components;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 25.08.13
 */
public class ByteArrayImage extends Image {

    public ByteArrayImage(String id, final IModel<byte[]> model) {
        super(id);
        if (model.getObject() != null) {
            setImageResource(new DynamicImageResource() {
                @Override
                protected byte[] getImageData(Attributes attributes) {
                    return model.getObject();
                }

            });
        } else {
            setImageResourceReference(new PackageResourceReference(ImportResourceLocator.class, "images/placeholder.jpg"));
        }
    }
}
