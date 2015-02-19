package fr.pierrezemb.isen.elementz;

import java.util.EventListener;

/**
 * Created by pierrezemb on 18/02/15.
 */
public interface ElementZ_Model_Listener extends EventListener {
    public void modelChanged(ModelChangedEvent event);
}
