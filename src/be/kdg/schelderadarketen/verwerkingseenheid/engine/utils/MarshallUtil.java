package be.kdg.schelderadarketen.verwerkingseenheid.engine.utils;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.StringReader;

public class MarshallUtil {

    public static <T> T parseMessage(Class<T> c, String message) {
        try {
            return c.cast(Unmarshaller.unmarshal(c, new StringReader(message)));
        } catch (MarshalException | ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
