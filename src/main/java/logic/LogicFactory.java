package logic;

import java.lang.reflect.InvocationTargetException;

//TODO this class is just a skeleton it must be completed
public abstract class LogicFactory {
    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    public static < T> T getFor( String entityName ) {
        try {
            String className = PACKAGE + entityName + SUFFIX;
            return (T)getFor(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static<T> T getFor(Class<T> cType)  {
        try {
            return (T) cType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
