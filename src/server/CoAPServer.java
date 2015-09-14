package server;

import org.eclipse.californium.core.CoapServer;
/**
 * Created by Patrick on 8/18/2015.
 */
public class CoAPServer {
    public static void main(String[] args){
        CoapServer simpleServer = new CoapServer();
        //simpleServer.add(new TimeResource("system-time"));
        simpleServer.add(new RegisterNode("register"));
        simpleServer.add(new RecordSensorReadings("recordSensorReading"));
        simpleServer.add(new RequestNodeConfiguration("getConfig"));
        simpleServer.start();
    }
}
