package server;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Patrick on 25/08/2015.
 */
public class NodeData extends CoapResource {

    private int sensor1, sensor2, nodeID;
    private String sensor3, sensor4;

    public NodeData(String name) {
        super(name);
    }

    @Override
    public void handlePOST(CoapExchange ex){
        ex.accept();
        String request = ex.getRequestText();
        System.out.println(request);
        parseNodeData(request.split(" "));
        postData();
        ex.respond(CoAP.ResponseCode.CREATED);
    }

    private void postData(){
        try {
            System.out.println(nodeID);
            String sql = "Insert into " + '"' + nodeID + '"' + " (N_ID, Sensor_1, Sensor_2, Sensor_3, Sensor_4) Values (" + nodeID + "," + sensor1 + "," + sensor2 + ",'" + sensor3 + "','" + sensor4 + "')";
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TEST");
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql);

            res.close();
            st.close();
            con.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseNodeData(String[] args){
        for(int i = 0; i < args.length; i++){
            switch (args[i]){
                case "-s1": {
                    sensor1 = Integer.parseInt(args[++i]);
                    break;
                }
                case "-s2": {
                    sensor2 = Integer.parseInt(args[++i]);
                    break;
                }
                case "-s3": {
                    sensor3 = args[++i];
                    break;
                }
                case "-s4": {
                    sensor4 = args[++i];
                    break;
                }
                case "-id": {
                    nodeID = Integer.parseInt(args[++i]);
                    break;
                }
                default:{
                    System.out.println("Unrecognized option: "+args[i]);
                    break;
                }
            }
        }
    }
}
