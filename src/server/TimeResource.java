package server;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.net.ResponseCache;
import java.util.Date;

/**
 * Created by Patrick on 8/18/2015.
 */
public class TimeResource extends CoapResource{

    public TimeResource(String name) {
        super(name);
    }

    @Override
    public void handleGET(CoapExchange ex){
        Date now = new Date();
        ex.respond("Server Time is: "+now.toString());
    }

    @Override
    public void handlePOST(CoapExchange ex){
        ex.accept();
        ex.respond(CoAP.ResponseCode.FORBIDDEN);
    }


}
