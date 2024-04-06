package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Components.Asset;
import Components.Message;
import javafx.collections.ObservableList;

public class DB_Messages extends DB_Utilities{
	
	//id
    private static int last_id = 0;
    
    public static void refresh(ObservableList<Message> obsList,Asset asset) {
    	try(Connection con = DB_Utilities.getDataSource().getConnection()){
			String getAllMessagesQuery = "SELECT * FROM messages WHERE cor_asset_id=?";
			try(PreparedStatement psAssets = con.prepareStatement(getAllMessagesQuery)){
				psAssets.setInt(1, asset.getAsset_id());
				try(ResultSet rs = psAssets.executeQuery()){
					while(rs.next()) {//while the reader still has a next row read it:
						int message_id = rs.getInt("message_id");
						String message = rs.getString("message");
						int cor_asset_id = rs.getInt("cor_asset_id");
						int message_author = rs.getInt("message_author");
						Date message_date = rs.getDate("message_date");
						
						Message msg = new Message(message_id, message, cor_asset_id, message_author, message_date);
						obsList.add(msg);
					}
				}
			}
		}catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
    }
    
	
	public static void addMessage(ObservableList<Message> obsList, Message msg) {
		try(Connection con = dataSource.getConnection()){
			String insertMessage = "INSERT INTO messages (message,cor_asset_id,message_author,message_date) VALUES (?,?,?,?)";
			try(PreparedStatement ps = con.prepareStatement(insertMessage,Statement.RETURN_GENERATED_KEYS)){
				
				ps.setString(1,msg.getMessage());
				ps.setInt(2,msg.getCor_asset_id());
				ps.setInt(3,msg.getMessage_author());
				ps.setDate(4,msg.getMessage_date());
				ps.executeUpdate();
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			            last_id = generatedKeys.getInt(1);
			            msg.setMessageid(last_id);
						obsList.add(msg);
			            
			         } else {
			        	 Helper.displayErrorMessage("Error","Failed to retrieve last inserted ID.");
			         }
			    }
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
}
