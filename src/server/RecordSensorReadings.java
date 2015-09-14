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
public class RecordSensorReadings extends CoapResource {

    //private Connection con;
    private int nodeID = -1;

    public RecordSensorReadings(String name) {
        super(name);
    }

    @Override
    public void handlePOST(CoapExchange ex){
        ex.accept();
        String request = ex.getRequestText();
        //System.out.println(request);
        int success = postData(request.split(","));
        if(success == -1) {
            ex.respond("-1");
        } else {
            updateNodeStatus();
            boolean updateConfig = checkForUpdatedConfig(nodeID);
            ex.respond(""+updateConfig);
        }
    }

    private int postData(String[] values){
        try {
            nodeID = Integer.parseInt(values[0]);
            //System.out.println(nodeID);
            String sql = "Insert into " + '"' + nodeID + '"' + " (N_ID, N_GPS, N_TIMESTAMP, TEMP_1, TEMP_2, RAINFALL, WINDSPEED) Values (" + nodeID + ",'" + values[1] + "', NOW(),'" + values[2] + "','" + values[3] + "','" + values[4] + "','" + values[5] + "')";

            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TEST");

            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql);

            res.close();
            st.close();
            con.close();

        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private void updateNodeStatus(){
        try {
            String sql = "UPDATE NODE_LIST SET N_LAST=NOW() WHERE N_ID='"+nodeID+"'";

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

    private boolean checkForUpdatedConfig(int n_id){
        boolean needs_update = false;
        String sql_query = "SELECT N_PUSH FROM NODE_LIST WHERE N_ID='"+n_id+"'";
        try{
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TEST");

            Statement state = con.createStatement();
            ResultSet res = state.executeQuery(sql_query);
            while(res.next())
                needs_update = res.getBoolean("N_PUSH");
            res.close();
            state.close();
            con.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return needs_update;
    }
}
