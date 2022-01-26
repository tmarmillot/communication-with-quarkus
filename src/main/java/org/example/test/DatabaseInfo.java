package org.example.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "pg_stat_activity")
public class DatabaseInfo {
    @Id
    @Column(name = "datid", nullable = false)
    public Long id;

    public String datname;
    public int pid;
    public Timestamp backend_start;

}
