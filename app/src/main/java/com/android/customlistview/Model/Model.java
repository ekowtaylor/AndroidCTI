package com.android.customlistview.Model;

public class Model {

    private String Image, Name, TableName, TableColumn;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tablename) {
        TableName = tablename;
    }


    public String getTableColumn() {
        return TableColumn;
    }

    public void setTableColumn(String tablecolumn) {
        TableColumn = tablecolumn;
    }


    public Model(String image, String name, String tablename, String tablecolumn) {
        Image = image;
        Name = name;
        TableName = tablename;
        TableColumn = tablecolumn;
    }
}
