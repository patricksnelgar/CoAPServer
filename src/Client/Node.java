package Client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.util.Random;

/**
 * Created by Patrick on 8/18/2015.
 */
public class Node extends CoapClient {
    public static void main(String[] args){
        Random rand = new Random();
        if(args.length <= 0){
            CoapClient node1 = new CoapClient("coap://127.0.0.1:5683/system-time");
            String response = node1.get().getResponseText();
            System.out.println(response);
        } else {
            CoapClient node1 = new CoapClient("coap://127.0.0.1:/register");
            String reg_info = "NodeID: "+Math.abs(rand.nextInt())+" NodeType: 0 NodeAddress: beef::02";
            CoapResponse resp = node1.post(reg_info, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(resp.isSuccess());
        }
    }
}
