package de.urlaubr.webapp.components.listeditor;

/**
 * Wicket ListEditor Component
 * Enables the user to add/edit/delete data to a list object.
 * Source: http://wicketinaction.com/2008/10/building-a-listeditor-form-component/
 *
 * @author Martijn Dashorst
 * @author Eelco Hillenius
 */
public class RemoveButton extends EditorButton {

    public RemoveButton(String id) {
        super(id);
        setDefaultFormProcessing(false);
    }

    @Override
    public void onSubmit() {
        int idx = getItem().getIndex();

        for (int i = idx + 1; i < getItem().getParent().size(); i++) {
            ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
            item.setIndex(item.getIndex() - 1);
        }

        getList().remove(idx);
        getEditor().remove(getItem());
    }

    @Override
    public boolean isEnabled() {
        return getEditor().checkRemove(getItem());
    }
}
