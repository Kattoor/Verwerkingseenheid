package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.Engine;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] argv) throws IOException, InterruptedException, TimeoutException {
        new Engine();
    }

    public static <T> T parseMessage(Class<T> c, String message) {
        try {
            System.out.println(message);
            return c.cast(Unmarshaller.unmarshal(c, new StringReader(message)));
        } catch (MarshalException | ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }
}