# JDataTable
 An ado.net-inspired DataTable class implemented in Java
 
## Download com-bblj-jdatatable.jar
https://drive.google.com/file/d/1IkpuZArKN4-jamdlpKYT0sqnFaSMIF3F/view?usp=sharing

## Usage
```
...(Connecting to Chinook.db)
Connection conn = DriverManager.getConnection(connString);
Statement stmt = conn.createStatement();
String sql = "SELECT * FROM albums";
ResultSet rs = stmt.executeQuery(sql);
DataTable dt = DataTable.fromResultSet(rs);  // <------- convert ResultSet to DataTable
rs.close();
stmt.close();
conn.close();

//Insert new row
DataRow newRow = dt.newRow();
newRow.setCellValue("AlbumId", 999);
newRow.setCellValue("Title", "Test1");
newRow.setCellValue("ArtistId", 1);
dt.getRows().add(0, newRow);

//Delete row
DataRow toDelete = dt.getRows().get(2);
toDelete.delete();

//Update row
DataRow toModified = dt.getRows().get(1);
toModified.setCellValue("Title", "xxxxxx");

//Loop through all data cells
for(DataRow row : dt.getRows()) {
    for(DataColumn col : dt.getColumns()) {
        System.out.print(row.getCellValue(col.getColumnName()));
        System.out.print("; ");
    }
    System.out.print(row.getRowState()); //Print row state (Added,Deleted,Detached,Modified,Unchanged)
    System.out.println();
}

//Convert to List<Map<String, Object>>
List<Map<String, Object>> rows = dt.toItemArrays();  //pass rows into org.json.JSONArray's constructor to create an json array
...
```
## Devlog
2022.10.30
1. First commit with the basic functionalities of DataTable.

2023.10.24
1. Adopt camal casing in naming methods.
2. Added 'toItemArrays' method to DataTable class.
