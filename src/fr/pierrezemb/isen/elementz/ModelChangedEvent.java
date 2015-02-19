package fr.pierrezemb.isen.elementz;

import java.util.EventObject;

/**
 * Created by pierrezemb on 18/02/15.
 */
public class ModelChangedEvent extends EventObject {

    public ModelChangedEvent(Object source){
        super(source);
    }
}
