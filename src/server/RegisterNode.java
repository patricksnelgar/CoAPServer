package server;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by Patrick on 8/18/2015.
 */
public class RegisterNode extends CoapResource {

    String db = "//localhost/wsn_db";
    static Connection connection;


    public RegisterNode(String name) {
        super(name);
    }

    @Override
    public void handleGET(CoapExchange ex){
        ex.accept();
        ex.respond(CoAP.ResponseCode.FORBIDDEN);
    }

    @Override
    public void handlePOST(CoapExchange ex) {
        ex.accept();
        String request = ex.getRequestText();
        writeToFile(request);
        writeToDB(request);
        ex.respond(CoAP.ResponseCode.CREATED);
    }

    private void writeToDB(String info){
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TAMIYA");

            String sql_query = parseInfo(info);
            System.out.println(sql_query);
            Statement st = null;
            ResultSet res = null;

            st = connection.createStatement();
            res = st.executeQuery(sql_query);

            res.close();
            st.close();
            connection.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String parseInfo(String nodeinfo){
        String[] split = nodeinfo.split(" ",'\n');
        int ID = -1;
        int type = -1;
        String address = null;
        for(int i = 0; i < split.length; i++){
            switch(split[i]){
                case "NodeID:":{
                    ID = Integer.parseInt(split[i+1]);
                    i++;
                    break;
                }
                case "NodeAddress:": {
                    address = split[i+1];
                    i++;
                    break;
                }
                case "NodeType:": {
                    type = Integer.parseInt(split[i+1]);
                    i++;
                    break;
                }

                default: {
                    System.out.println("Unrecognized parameter: "+split[i]);
                }
            }
        }

        return "INSERT INTO NODE_LIST (N_ID, N_Address, N_Type, N_Battery) VALUES("+ID+",'"+address+"',"+type+",100)";
    }

    private void writeToFile(String info){
        try {
            PrintWriter nodelist = new PrintWriter(new BufferedWriter(new FileWriter("node_list.txt", true)));
            nodelist.println(info);
            nodelist.flush();
            nodelist.close();

        } catch (Exception e){
            System.out.println("Error writing to file:\nFull Trace:\n"+e.getStackTrace());
        }
    }
}
