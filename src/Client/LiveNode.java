package Client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.sql.ResultSet;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by Patrick on 14/09/2015.
 */
public class LiveNode {

    final int DEFAULT_EXECUTION_INTERVAL = 10;
    int execution_Interval = DEFAULT_EXECUTION_INTERVAL;
    private ScheduledExecutorService scheduler;
    private Runnable task;
    private CoapResponse response;
    private CoapClient node;
    private int nodeID = 10, battery = 100, type = 0;
    private String address = "beef::10", friendly = "weather 1", gps = "-37.77 175.31";
    private String temp_1, temp_2, rainfall, windspeed;



    public static void main(String[] args){
        (new LiveNode()).run();
    }

    public void run(){
        System.out.println("Node booting...");

        response = registerNode();

        if(Integer.parseInt(response.getResponseText()) < 0 ){
            System.out.println("Error registering node");
            System.exit(1);
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();

        task = new Runnable() {
            @Override
            public void run() {
                try{
                    //Place to execute posting node data.
                    System.out.println("Posting sensor measurements.... " + execution_Interval);
                    String res = postSensorInformation().getResponseText();

                    if(res.length() < 3) {
                        if (Integer.parseInt(res) < 0){
                            System.out.println("Error posting data");
                            System.exit(1);
                        }
                    } else {
                        //System.out.println(":" + res + ":");
                        if(Boolean.parseBoolean(res)==true){
                            requestNewConfig();
                        } else {
                            //Everything is OK
                            //System.out.println("!=true");
                        }
                    }
                    //changeTimer();
                }catch(Exception e){
                    e.printStackTrace(); //Debug purposes.
                } finally {
                    scheduler.schedule(this, execution_Interval, TimeUnit.SECONDS);
                }
            }
        };

        scheduler.schedule(task, execution_Interval, TimeUnit.SECONDS);
    }

    private void requestNewConfig(){
        System.out.println("Requesting new configuration....");
        System.out.println("Old config = " + execution_Interval);
        node = new CoapClient("coap://127.0.0.1:/getConfig");
        String payload = nodeID + "";
        CoapResponse res = node.post(payload, MediaTypeRegistry.TEXT_PLAIN);
        String[] values = res.getResponseText().split(",");
        execution_Interval = Integer.parseInt(values[1]);
        System.out.println("New config = " + execution_Interval);
    }

    private CoapResponse registerNode(){
        node = new CoapClient("coap://127.0.0.1:/register");
        String payload = nodeID + "," + address + "," + type + "," + battery + "," + execution_Interval + "," + friendly;
        return node.post(payload, MediaTypeRegistry.TEXT_PLAIN);
    }

    private CoapResponse postSensorInformation(){
        node = new CoapClient("coap://127.0.0.1/recordSensorReading");
        getSensorReadings();
        String payload = nodeID + "," + gps + "," + temp_1 + "," + temp_2 + "," + rainfall + "," + windspeed;
        return node.post(payload, MediaTypeRegistry.TEXT_PLAIN);
    }

    private void getSensorReadings(){
        Random r = new Random();
        temp_1 = String.valueOf(r.nextInt(35))+"C";
        temp_2 = String.valueOf(r.nextInt(35))+"C";

        rainfall = String.valueOf(r.nextInt(100))+"mL/h";
        windspeed = String.valueOf(r.nextInt(100))+"m/s";
    }
}
