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

    @ColumnInfo(name = "profile_pic", typeAffinity = ColumnInfo.BLOB)
    public byte[] profilePic;

    public Person(int personId, String name, byte[] profilePic) {
        this.personId = personId;
        this.name = name;
        this.profilePic = profilePic;
    }
}
