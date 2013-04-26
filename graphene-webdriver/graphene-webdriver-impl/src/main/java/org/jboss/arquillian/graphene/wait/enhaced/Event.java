package org.jboss.arquillian.graphene.wait.enhaced;

/**
 * Encapsulates event definitions from DOM Events model.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class Event {
    /** The event definition for 'abort' */
    public static final Event ABORT = new Event("abort");
    /** The event definition for 'blur' */
    public static final Event BLUR = new Event("blur");
    /** The event definition for 'change' */
    public static final Event CHANGE = new Event("change");
    /** The event definition for 'click' */
    public static final Event CLICK = new Event("click");
    /** The event definition for 'dblclick' */
    public static final Event DBLCLICK = new Event("dblclick");
    /** The event definition for 'error' */
    public static final Event ERROR = new Event("error");
    /** The event definition for 'focus' */
    public static final Event FOCUS = new Event("focus");
    /** The event definition for 'keydown' */
    public static final Event KEYDOWN = new Event("keydown");
    /** The event definition for 'keypress' */
    public static final Event KEYPRESS = new Event("keypress");
    /** The event definition for 'keyup' */
    public static final Event KEYUP = new Event("keyup");
    /** The event definition for 'load' */
    public static final Event LOAD = new Event("load");
    /** The event definition for 'mousedown' */
    public static final Event MOUSEDOWN = new Event("mousedown");
    /** The event definition for 'mousemove' */
    public static final Event MOUSEMOVE = new Event("mousemove");
    /** The event definition for 'mouseout' */
    public static final Event MOUSEOUT = new Event("mouseout");
    /** The event definition for 'mouseover' */
    public static final Event MOUSEOVER = new Event("mouseover");
    /** The event definition for 'mouseup' */
    public static final Event MOUSEUP = new Event("mouseup");
    /** The event definition for 'reset' */
    public static final Event RESET = new Event("reset");
    /** The event definition for 'resize' */
    public static final Event RESIZE = new Event("resize");
    /** The event definition for 'select' */
    public static final Event SELECT = new Event("select");
    /** The event definition for 'submit' */
    public static final Event SUBMIT = new Event("submit");
    /** The event definition for 'unload' */
    public static final Event UNLOAD = new Event("unload");

    /** The event name. */
    private String eventName;

    /**
     * <p>
     * Initializes new event.
     * </p>
     *
     * <p>
     * Use for extending event model as it can became out-of-date.
     * </p>
     *
     * @param eventName
     *            the name for event in DOM Event model
     */
    public Event(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Returns the name of this event as it is specified in DOM Event model.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return eventName;
    }
}
