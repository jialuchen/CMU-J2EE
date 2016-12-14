/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class Action {
    public abstract String getName();

    public abstract String perform(HttpServletRequest request);

    private static Map<String, Action> hash = new HashMap<String, Action>();

    public static void add(Action a) {
        synchronized (hash) {
            hash.put(a.getName(), a);
        }
    }

    public static String perform(String name, HttpServletRequest request) {
        Action a;
        synchronized (hash) {
            a = hash.get(name);
        }

        if (a == null) {
        	return null;
        }
        
        return a.perform(request);
    }
}
