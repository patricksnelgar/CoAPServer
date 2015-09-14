package server;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Patrick on 14/09/2015.
 */
public class RequestNodeConfiguration extends CoapResource {

    private int n_id, interval;

    public RequestNodeConfiguration(String name) {
        super(name);
    }

    @Override
    public void handlePOST(CoapExchange ex){
        ex.accept();
        n_id = Integer.parseInt(ex.getRequestText());
        int success = getNewConfig();
        if(success < 0){
            ex.respond("-1");
        } else{
            ex.respond("interval," + interval);
            setConfigUpdated();
        }
    }

    private int getNewConfig(){
        try{
            String sql_query = "SELECT N_INTERVAL FROM NODE_LIST WHERE N_ID='" + n_id + "'";
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TEST");

            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql_query);
            while (res.next()) {
                interval = res.getInt("N_INTERVAL");
            }
            res.close();
            st.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private void setConfigUpdated(){
        try{
            String sql_query = "UPDATE NODE_LIST SET N_PUSH=false WHERE N_ID='" + n_id + "'";
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TEST");

            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql_query);

            res.close();
            st.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
