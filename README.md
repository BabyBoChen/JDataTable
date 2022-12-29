# JDataTable
 An ado.net-like Datatable class implemented in Java
 
## Download com-bblj-jdatatable.jar
https://drive.google.com/file/d/1IkpuZArKN4-jamdlpKYT0sqnFaSMIF3F/view?usp=sharing

## Usage
```
...(Connecting to Chinook.db)
Connection conn = DriverManager.getConnection(connString);
Statement stmt = conn.createStatement();
String sql = "SELECT * FROM albums";
ResultSet rs = stmt.executeQuery(sql);
DataTable dt = DataTable.FromResultSet(rs);  // <------- convert ResultSet to DataTable
rs.close();
stmt.close();
conn.close();

//Insert new row
DataRow newRow = dt.NewRow();
newRow.SetCellValue("AlbumId", 999);
newRow.SetCellValue("Title", "Test1");
newRow.SetCellValue("ArtistId", 1);
dt.getRows().add(0, newRow);

//Delete row
DataRow toDelete = dt.getRows().get(2);
toDelete.Delete();

//Update row
DataRow toModified = dt.getRows().get(1);
toModified.SetCellValue("Title", "xxxxxx");

//Loop through all data cells
for(DataRow row : dt.getRows()) {
    for(DataColumn col : dt.getColumns()) {
        System.out.print(row.GetCellValue(col.getColumnName()));
        System.out.print("; ");
    }
    System.out.print(row.getRowState()); //Print row state (Added,Deleted,Detached,Modified,Unchanged)
    System.out.println();
}
...
```
## Devlog
2022.10.30
1. First commit with the basic functionalities of DataTable.
