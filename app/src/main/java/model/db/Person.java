package model.db;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity(tableName = "persons")
public class Person {
    @PrimaryKey
    @ColumnInfo(name = "person_id")
    public int personId;

    @ColumnInfo(name = "name")
    public String name;

    //@ColumnInfo(name = "profilePic", typeAffinity = ColumnInfo.BLOB)
    //private byte[] profilePic;
}
