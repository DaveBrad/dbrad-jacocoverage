/** Copyright (c) 2017 dbradley.
 *
 * This is modified code (comments and Java 8 level)
 *
 * Original code was from:
 *
 * The WrappedAction class is provide at https://tips4java.wordpress.com
 * 'Java Tips Weblog' which provides the code as-is:
 *
 * 'We assume no responsibility for the code. You are free to use and/or
 * modify and/or distribute any or all code posted on the Java Tips Weblog
 * without restriction. A credit in the code comments would be nice, but
 * not in any way mandatory.'
 *
 */
package dbrad.jacocoverage.plugin.config;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * The WrappedAction class is a convenience class that allows you to replace an
 * installed Action with a custom Action of your own. There are two benefits to
 * using this class:
 * <p>
 * a) Key Bindings of the original Action are retained for the custom Action.
 * <p>
 * b) the original Action is retained so your custom Action can invoke the
 * original Action.
 * <p>
 * This class is abstract so your custom Action must extend this class and
 * implement the actionPerformed() method.
 */
public abstract class WrappedAction implements Action {

    /** the component the action is processed upon */
    private final JComponent component;

    /** 
     * the actionMap from JComponents has three (3) conditions
     * for Window inputMap mapping
     */
    private static int[] actionMapIndexesArr = new int[]{
        JComponent.WHEN_IN_FOCUSED_WINDOW,
        JComponent.WHEN_FOCUSED,
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT};

    /** the original action instance so they can be applied */
    private Action originalAction;

    /** The action key that is being wrapped */
    private Object actionKey;

    /*
	 *  Replace the default Action for the given KeyStroke with a custom Action
     */
    public WrappedAction(JComponent componentP, KeyStroke keyStroke) {
        this.component = componentP;
        Object actionKeyL = getKeyForActionMap(componentP, keyStroke);

        if (actionKeyL == null) {
            String message = "No input mapping for KeyStroke: " + keyStroke;
            throw new IllegalArgumentException(message);
        }
        setCustomActionForKey(actionKeyL);
    }

    /*
	 *  Replace the default Action with a custom Action
     */
    public WrappedAction(JComponent componentP, Object actionKeyP) {
        this.component = componentP;
        setCustomActionForKey(actionKeyP);
    }

    /*
	 *  Search the 3 InputMaps to find the KeyStroke binding
     */
    private Object getKeyForActionMap(JComponent component, KeyStroke keyStroke) {
        // orginal code was 'for (int i = 0; i < 3; i++) {' using a magic number 3
        // 
        // the 3 magic refers to JComponent constants which are indexes
        // WHEN_IN_FOCUSED_WINDOW, WHEN_FOCUSED, WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
       
        for (int i : actionMapIndexesArr) {
            InputMap inputMap = component.getInputMap(i);

            if (inputMap != null) {
                Object key = inputMap.get(keyStroke);

                if (key != null) {
                    return key;
                }
            }
        }
        return null;
    }

    /*
	 *  Replace the existing Action for the given action key with a
	 *  wrapped custom Action
     */
    private void setCustomActionForKey(Object actionKeyP) {
        //  Save the original Action

        this.actionKey = actionKeyP;
        originalAction = component.getActionMap().get(actionKeyP);

        if (originalAction == null) {
            String message = "no Action for action key: " + actionKeyP;
            throw new IllegalArgumentException(message);
        }
        //  Replace the existing Action with this class
        installCustom();
    }

    /*
	 *  Child classes should use this method to invoke the original Action
     */
    public void invokeOriginalAction(ActionEvent e) {
        originalAction.actionPerformed(e);
    }

    /*
	 *  Install this class as the default Action
     */
    public void installCustom() {
        component.getActionMap().put(actionKey, this);
    }

    /*
	 *	Restore the original Action as the default Action
     */
    public void unInstallCustom() {
        component.getActionMap().put(actionKey, originalAction);
    }

    //
    //  Delegate the Action interface methods to the original Action
    //
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        originalAction.addPropertyChangeListener(listener);
    }

    @Override
    public Object getValue(String key) {
        return originalAction.getValue(key);
    }

    @Override
    public boolean isEnabled() {
        return originalAction.isEnabled();
    }

    @Override
    public void putValue(String key, Object newValue) {
        originalAction.putValue(key, newValue);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        originalAction.removePropertyChangeListener(listener);
    }

    @Override
    public void setEnabled(boolean newValue) {
        originalAction.setEnabled(newValue);
    }

    //
    //  Implement some AbstractAction methods
    //
    public Object[] getKeys() {
        if (originalAction instanceof AbstractAction) {
            AbstractAction abstractAction = (AbstractAction) originalAction;
            return abstractAction.getKeys();
        }
        return null;
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (originalAction instanceof AbstractAction) {
            AbstractAction abstractAction = (AbstractAction) originalAction;
            return abstractAction.getPropertyChangeListeners();
        }
        return null;
    }
}
