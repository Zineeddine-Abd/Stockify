package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Components.Action;
import Components.Asset;
import javafx.collections.ObservableList;

public class DB_Actions {
	 public static void refresh(ObservableList<Action> obsList) {
	    	try(Connection con = DB_Utilities.getDataSource().getConnection()){
				String getAllActionsQuery = "SELECT * FROM assets";
				try(PreparedStatement psActions = con.prepareStatement(getAllActionsQuery)){
					try(ResultSet rs = psActions.executeQuery()){
						while(rs.next()) {//while the reader still has a next row read it:
							//retrieve all Actions from the db.
						}
					}
				}
			}catch (SQLException e) {
				Helper.displayErrorMessage("Error",e.getMessage());
			}
	    }
}
