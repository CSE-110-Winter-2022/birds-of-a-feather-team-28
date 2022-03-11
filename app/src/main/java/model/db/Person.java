package model.db;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

import java.util.UUID;

@Entity(tableName = "persons")
public class Person {
    @PrimaryKey
    @ColumnInfo(name = "person_id")
    public UUID personId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "profile_pic")
    public String profilePic;

    public Person(UUID personId, String name, String profilePic) {
        this.personId = personId;
        this.name = name;
        this.profilePic = profilePic;
    }
}
