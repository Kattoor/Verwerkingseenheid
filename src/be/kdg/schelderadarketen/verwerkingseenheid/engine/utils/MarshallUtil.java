package be.kdg.schelderadarketen.verwerkingseenheid.engine.utils;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class MarshallUtil {

    public static <T> T unmarshall(Class<T> c, String message) {
        try {
            return c.cast(Unmarshaller.unmarshal(c, new StringReader(message)));
        } catch (MarshalException | ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String marshall(T object) {
        Writer w = new StringWriter();
        try {
            Marshaller.marshal(object, w);
            return w.toString();
        } catch (MarshalException | ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
