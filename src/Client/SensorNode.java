package Client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

/**
 * Created by Patrick on 25/08/2015.
 */
public class SensorNode extends CoapClient {

    public static void main(String[] args){
        if(args.length <= 0) printHelp();
        else {
            CoapClient node = new CoapClient("coap://127.0.0.1/postNodeData");
            String data = Arrays.toString(args).replaceAll(",", "").replaceAll("\\[","").replaceAll("\\]", "");
            System.out.println(Arrays.toString(args).replaceAll(",", "").replaceAll("\\[","").replaceAll("\\]",""));

            CoapResponse res = node.post(data, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(res.isSuccess());
        }
    }

    private static void printHelp(){
        System.out.println("Enter values for different sensors (-s1, -s2, -s3, -s4, -id)");
    }
}
