package com.bblj.jdatatable.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.bblj.jdatatable.*;

public class Test {

	public static void main(String[] args) {
	//test
	String cwd = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	if(cwd.startsWith("/")){
		cwd = cwd.substring(1);
	}
	Path pwd = Paths.get(cwd);
	Path dbPath = Paths.get(pwd.toAbsolutePath().toString(), "Chinook.db");
	String connString = "jdbc:sqlite:" + dbPath;
    try {
    	Class.forName("org.sqlite.JDBC");
    	Connection conn = DriverManager.getConnection(connString);
    	Statement stmt = conn.createStatement();
    	String sql = "SELECT * FROM albums";
    	ResultSet rs = stmt.executeQuery(sql);
    	DataTable dt = DataTable.FromResultSet(rs);
    	rs.close();
    	stmt.close();
    	conn.close();
    	DataRow newRow = dt.NewRow();
    	newRow.SetCellValue("AlbumId", 999);
    	newRow.SetCellValue("Title", "Test1");
    	newRow.SetCellValue("ArtistId", 1);
    	dt.getRows().add(0, newRow);
    	DataRow toDelete = dt.getRows().get(2);
    	toDelete.Delete();
    	DataRow toModified = dt.getRows().get(1);
    	toModified.SetCellValue("Title", "xxxxxx");
    	for(DataRow row : dt.getRows()) {
    		for(DataColumn col : dt.getColumns()) {
    			System.out.print(row.GetCellValue(col.getColumnName()));
    			System.out.print("; ");
        	}
    		System.out.print(row.getRowState());
    		System.out.println();
		}
    } catch (Exception ex) {
        System.out.println(ex.getMessage());
    } 
}

}
