package de.urlaubr.webapp.components.listeditor;

import org.apache.wicket.markup.html.form.Button;

import java.util.List;

/**
 * Wicket ListEditor Component
 * Enables the user to add/edit/delete data to a list object.
 * Source: http://wicketinaction.com/2008/10/building-a-listeditor-form-component/
 *
 * @author Martijn Dashorst
 * @author Eelco Hillenius
 */
public abstract class EditorButton extends Button {

    private transient ListItem<?> parent;

    public EditorButton(String id) {
        super(id);
    }

    protected final ListItem<?> getItem() {
        if (parent == null) {
            parent = findParent(ListItem.class);
        }
        return parent;
    }

    protected final List<?> getList() {
        return getEditor().items;
    }

    protected final ListEditor<?> getEditor() {
        return (ListEditor<?>) getItem().getParent();
    }

    @Override
    protected void onDetach() {
        parent = null;
        super.onDetach();
    }

}