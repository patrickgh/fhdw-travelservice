package de.urlaubr.webapp.components;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

/**
 * ByteArrayImage component for wicket.
 * Displays an image from an byte array. If the model object is null, it displays a placeholder instead.
 *
 * @author Patrick Groß-Holtwick
 *         Date: 25.08.13
 */
public class ByteArrayImage extends NonCachingImage {

    private IModel<byte[]> model;

    public ByteArrayImage(String id) {
        this(id, null);
    }

    public ByteArrayImage(String id, final IModel<byte[]> model) {
        super(id);
        this.model = model;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (model != null && model.getObject() != null) {
            setImageResource(new DynamicImageResource() {
                @Override
                protected byte[] getImageData(Attributes attributes) {
                    return model.getObject();
                }

            });
        }
        else {
            setImageResourceReference(new PackageResourceReference(ImportResourceLocator.class, "images/placeholder.jpg"));
        }
    }
}
